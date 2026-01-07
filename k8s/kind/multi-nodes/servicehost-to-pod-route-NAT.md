# DNS
- `cluster.local` is the default DNS domain for Kubernetes services and pods within a cluster. It is used by CoreDNS to resolve internal service and pod names
    - record format `<service name>.<namespace>.svc.cluster.local`, example: `nginx-service.default.svc.cluster.local`
- How service domain name reaches the pod
    1. The service DNS name resolves to a **ClusterIP**
    2. Requests to 10.96.192.176 will be routed by kube-proxy to one of the nginx pods behind the service
    3. The pod receives the request as if it was sent directly to its IP and port
- kube-proxy's routing table
    ```
    iptables -t nat -L -n
    ```

## coredns configmap
```
% kubectl -n kube-system get configmap coredns -o yaml

apiVersion: v1
data:
  Corefile: |
    .:53 {
        errors
        health {
           lameduck 5s
        }
        ready
        kubernetes cluster.local in-addr.arpa ip6.arpa {
           pods insecure
           fallthrough in-addr.arpa ip6.arpa
           ttl 30
        }
        prometheus :9153
        forward . /etc/resolv.conf {
           max_concurrent 1000
        }
        cache 30 {
           disable success cluster.local
           disable denial cluster.local
        }
        loop
        reload
        loadbalance
    }
kind: ConfigMap
metadata:
  creationTimestamp: "2026-01-04T14:17:43Z"
  name: coredns
  namespace: kube-system
  resourceVersion: "226"
  uid: a4b3cafd-7ac5-45cf-8682-78a298188ef8
```

## Iptable in a Node
A chain contains some rules, when matching would trigger the next chain. 

### Explanation
- Prot samples:
    - 6: TCP (protocol number 6)
    - 17: UDP (protocol number 17)
    - all or 0: any protocol
- last column
    - `dpt` (destination port) is a keyword used in iptables rules as a match condition—it specifies which port the rule should match
    - `to` is a keyword used in the action part of DNAT/SNAT rules—it specifies the new destination (for DNAT) or source (for SNAT) address and port to rewrite the packet to
- Standard Chain Purpose (Linux iptables standard):
    - `PREROUTING`: For packets as they come in, before a routing decision is made
    - `OUTPUT`: For locally-generated packets leaving the system
    - [ref](https://linux.die.net/man/8/iptables)

### Steps
1. `KUBE-SERVICES` is triggered during `PREROUTING` and `OUTPUT`
    ```
    Chain PREROUTING (policy ACCEPT)
    target     prot opt source               destination         
    KUBE-SERVICES  0    --  0.0.0.0/0            0.0.0.0/0            /* kubernetes service portals */
    
    ...

    Chain OUTPUT (policy ACCEPT)
    target     prot opt source               destination         
    KUBE-SERVICES  0    --  0.0.0.0/0            0.0.0.0/0            /* kubernetes service portals */
    ```
2. `KUBE-SERVICES` contains several rule, `KUBE-SVC-V2OKYYMBY3REGZOG` would be triggered for any IP packet to `10.96.192.176`
    ```
    Chain KUBE-SERVICES (2 references)
    target     prot opt source               destination         
    KUBE-SVC-V2OKYYMBY3REGZOG  6    --  0.0.0.0/0            10.96.192.176        /* default/nginx-service cluster IP */ tcp dpt:80
    KUBE-SVC-TCOU7JCQXEZGVUNU  17   --  0.0.0.0/0            10.96.0.10           /* kube-system/kube-dns:dns cluster IP */ udp dpt:53
    KUBE-SVC-ERIFXISQEP7F7OF4  6    --  0.0.0.0/0            10.96.0.10           /* kube-system/kube-dns:dns-tcp cluster IP */ tcp dpt:53
    KUBE-SVC-JD5MR3NA4I4DYORP  6    --  0.0.0.0/0            10.96.0.10           /* kube-system/kube-dns:metrics cluster IP */ tcp dpt:9153
    KUBE-SVC-NPX46M4PTMTKRN6Y  6    --  0.0.0.0/0            10.96.0.1            /* default/kubernetes:https cluster IP */ tcp dpt:443
    KUBE-NODEPORTS  0    --  0.0.0.0/0            0.0.0.0/0            /* kubernetes service nodeports; NOTE: this must be the last rule in this chain */ ADDRTYPE match dst-type LOCAL
    ```
3. `KUBE-SVC-V2OKYYMBY3REGZOG` contains rules for load-balancing and DNAT to the actual backend pods
    - `KUBE-MARK-MASQ`: the request comes from outside the pod network (e.g., from a node, external client, or host process)
    - `KUBE-SEP-*`: else
    ```
    Chain KUBE-SVC-V2OKYYMBY3REGZOG (2 references)
    target                      prot opt source               destination         
    KUBE-MARK-MASQ              6    -- !10.244.0.0/16        10.96.192.176        /* default/nginx-service cluster IP */ tcp dpt:80
    KUBE-SEP-RHJMXRHDBGUC5TL7   0    --  0.0.0.0/0            0.0.0.0/0            /* default/nginx-service -> 10.244.1.3:80 */ statistic mode random probability 0.33333333349
    KUBE-SEP-XOMS3IZLIRPKWCVB   0    --  0.0.0.0/0            0.0.0.0/0            /* default/nginx-service -> 10.244.2.3:80 */ statistic mode random probability 0.50000000000
    KUBE-SEP-2JRCJNV76FX7SIRH   0    --  0.0.0.0/0            0.0.0.0/0            /* default/nginx-service -> 10.244.3.2:80 */
    ```
4. `KUBE-SEP-XOMS3IZLIRPKWCVB`
    - `KUBE-MARK-MASQ`: If the source IP is the pod itself (10.244.2.3), mark the packet for SNAT (MASQUERADE) later, ensuring correct return routing when a pod accesses its own service via ClusterIP
    - `DNAT`: For all TCP packets, perform Destination NAT to 10.244.2.3:80 (the pod’s IP and port). This sends the traffic to the actual pod
    ```
    Chain KUBE-SEP-XOMS3IZLIRPKWCVB (1 references)
    target          prot opt source               destination         
    KUBE-MARK-MASQ  0    --  10.244.2.3           0.0.0.0/0            /* default/nginx-service */
    DNAT            6    --  0.0.0.0/0            0.0.0.0/0            /* default/nginx-service */ tcp to:10.244.2.3:80
    ```
