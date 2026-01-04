# K8s Network Virtualization

## IP Addresses
- veth* (e.g., veth1866ccc2, vetha7b6163e, veth5f8bbed3)
    - Virtual Ethernet (veth) pairs, each connecting the host network namespace to a pod’s network namespace via CNI (Container Network Interface). 
    - All have the same IP (10.244.0.1/32), which is a common pattern in Kubernetes overlay networks
    - Each veth is the host-side of a **pod**'s network connection.
- eth0: The main Ethernet interface for the **node**, with IP 172.18.0.2/16 (likely the node’s address in the Docker or Kind network)
- tunl0 and ip6tnl0: Tunnel interfaces, typically used by network overlays (like Flannel) in Kubernetes, but currently down

```bash
root@my-kind-control-plane:/etc/kubernetes# ip address
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host 
       valid_lft forever preferred_lft forever

2: tunl0@NONE: <NOARP> mtu 1480 qdisc noop state DOWN group default qlen 1000
    link/ipip 0.0.0.0 brd 0.0.0.0
3: ip6tnl0@NONE: <NOARP> mtu 1452 qdisc noop state DOWN group default qlen 1000
    link/tunnel6 :: brd :: permaddr d28f:bb17:dcb6::

4: veth1866ccc2@if4: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 7e:50:4e:cf:31:f5 brd ff:ff:ff:ff:ff:ff link-netns cni-5e85041d-6854-a202-ad7b-41677cb9b412
    inet 10.244.0.1/32 scope global veth1866ccc2
       valid_lft forever preferred_lft forever
5: vetha7b6163e@if4: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 8a:f2:d2:19:48:06 brd ff:ff:ff:ff:ff:ff link-netns cni-4ee75c17-172b-430f-403a-318a585c59e4
    inet 10.244.0.1/32 scope global vetha7b6163e
       valid_lft forever preferred_lft forever
6: veth5f8bbed3@if4: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether aa:84:cf:45:fb:fa brd ff:ff:ff:ff:ff:ff link-netns cni-f6e0eebb-49d2-6411-5c8a-0348de1ebc92
    inet 10.244.0.1/32 scope global veth5f8bbed3
       valid_lft forever preferred_lft forever

20: eth0@if21: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:12:00:02 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.18.0.2/16 brd 172.18.255.255 scope global eth0
       valid_lft forever preferred_lft forever
    inet6 fc00:f853:ccd:e793::2/64 scope global nodad 
       valid_lft forever preferred_lft forever
    inet6 fe80::42:acff:fe12:2/64 scope link 
       valid_lft forever preferred_lft forever
```

## IP routes
```
root@my-kind-control-plane:/# ip route show
default via 172.18.0.1 dev eth0
10.244.0.2 dev veth1866ccc2 scope host
10.244.0.3 dev vetha7b6163e scope host
10.244.0.4 dev veth5f8bbed3 scope host
172.18.0.0/16 dev eth0 proto kernel scope link src 172.18.0.2
```

## Pod IPs
- `CoreDNS` gets a pod IP from the overlay network (10.244.x.x), and its traffic is routed through a veth pair created by the CNI plugin (like Flannel or Calico)
    - This veth connects the pod’s network namespace to the host, allowing pod-to-pod communication across nodes
    - **Normal pod (overlay) network**
- The other system pods (etcd, kube-apiserver, etc.) are static pods running directly on the node
    - These static pods run in the host network namespace (hostNetwork: true), so they use the node’s main interface (eth0) and share its IP (172.18.0.2)
    - **Host network**

```bash
% kubectl --context kind-my-kind -n kube-system get pods -o wide
NAME                                            READY   STATUS    RESTARTS   AGE   IP           NODE                    NOMINATED NODE   READINESS GATES
coredns-7d764666f9-ftwss                        1/1     Running   0          80m   10.244.0.3   my-kind-control-plane   <none>           <none>
coredns-7d764666f9-r5p9k                        1/1     Running   0          80m   10.244.0.4   my-kind-control-plane   <none>           <none>
etcd-my-kind-control-plane                      1/1     Running   0          81m   172.18.0.2   my-kind-control-plane   <none>           <none>
kindnet-n4skb                                   1/1     Running   0          80m   172.18.0.2   my-kind-control-plane   <none>           <none>
kube-apiserver-my-kind-control-plane            1/1     Running   0          81m   172.18.0.2   my-kind-control-plane   <none>           <none>
kube-controller-manager-my-kind-control-plane   1/1     Running   0          81m   172.18.0.2   my-kind-control-plane   <none>           <none>
kube-proxy-bk2bd                                1/1     Running   0          80m   172.18.0.2   my-kind-control-plane   <none>           <none>
kube-scheduler-my-kind-control-plane            1/1     Running   0          81m   172.18.0.2   my-kind-control-plane   <none>           <none>
```
