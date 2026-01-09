## Docker network between nodes
```
% docker network ls
NETWORK ID     NAME                   DRIVER    SCOPE
2923ae065c76   bridge                 bridge    local
98f9aa99b361   host                   host      local
ea4c761513a9   kind                   bridge    local
015c72f161dd   minikube               bridge    local
5328f50ca3e4   none                   null      local
df993f241d4d   policy-store_default   bridge    local
d0d67bc701d1   sync_default           bridge    local


% docker network inspect kind
[
    {
        "Name": "kind",
        "Id": "ea4c761513a9f84610d30732f9adaf5894ec1575a7b43841fbf066de5a789154",
        "Created": "2024-07-16T10:27:19.653539838Z",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv6": true,
        "IPAM": {
            "Driver": "default",
            "Options": {},
            "Config": [
                {
                    "Subnet": "172.18.0.0/16",
                    "Gateway": "172.18.0.1"
                },
                {
                    "Subnet": "fc00:f853:ccd:e793::/64",
                    "Gateway": "fc00:f853:ccd:e793::1"
                }
            ]
        },
        "Internal": false,
        "Attachable": false,
        "Ingress": false,
        "ConfigFrom": {
            "Network": ""
        },
        "ConfigOnly": false,
        "Containers": {
            "23752c79b2fa8df3a319acc83e21ef12e965288481ec6100c69983900ac52f7b": {
                "Name": "mutinode-control-plane",
                "EndpointID": "d68bccfde79cb11c5743883b948750c8b28fcca8a4a122bd1a68142a7373f882",
                "MacAddress": "02:42:ac:12:00:05",
                "IPv4Address": "172.18.0.5/16",
                "IPv6Address": "fc00:f853:ccd:e793::5/64"
            },
            "548b4118d40d7ef6f5903acf8d10658bea36e17283e5268a223917e7a1fe0a85": {
                "Name": "mutinode-worker2",
                "EndpointID": "d619073c19df7af76fa70846cd93d0587199b0493de1872a647d6c29ff1eadb8",
                "MacAddress": "02:42:ac:12:00:02",
                "IPv4Address": "172.18.0.2/16",
                "IPv6Address": "fc00:f853:ccd:e793::2/64"
            },
            "5d211b396f07d97f3c7ed6a61c004fbab1fdff82a49d95d71f45a1e7670a3631": {
                "Name": "mutinode-worker3",
                "EndpointID": "ef74faa67230d45f6711a29f091f3ffca0bae3cfdab3082ce58a745305971e26",
                "MacAddress": "02:42:ac:12:00:03",
                "IPv4Address": "172.18.0.3/16",
                "IPv6Address": "fc00:f853:ccd:e793::3/64"
            },
            "e1b411e8aa231a547c100908915d66cec5eeb823bb39a8e91d0288f8c4ce5b57": {
                "Name": "mutinode-worker",
                "EndpointID": "7738de74fa2cd41c3c8e4ba305c62e8f4602afb363eeb5d936ceca2976695ec4",
                "MacAddress": "02:42:ac:12:00:04",
                "IPv4Address": "172.18.0.4/16",
                "IPv6Address": "fc00:f853:ccd:e793::4/64"
            }
        },
        "Options": {
            "com.docker.network.bridge.enable_ip_masquerade": "true",
            "com.docker.network.driver.mtu": "1500"
        },
        "Labels": {}
    }
]
```

## Node NIC (MAC mapped to the Docker network) -- `02:42:ac:12:00:02`
```
root@mutinode-worker2:/# ip address
...
26: eth0@if27: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:12:00:02 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.18.0.2/16 brd 172.18.255.255 scope global eth0
       valid_lft forever preferred_lft forever
    inet6 fc00:f853:ccd:e793::2/64 scope global nodad 
       valid_lft forever preferred_lft forever
    inet6 fe80::42:acff:fe12:2/64 scope link 
       valid_lft forever preferred_lft forever


root@mutinode-worker2:/# cat /etc/cni/net.d/10-kindnet.conflist
{
	"cniVersion": "0.3.1",
	"name": "kindnet",
	"plugins": [
        {
            "type": "ptp",
            "ipMasq": false,
            "ipam": {
                "type": "host-local",
                "dataDir": "/run/cni-ipam-state",
                "routes": [
                    { "dst": "0.0.0.0/0" }
                ],
                "ranges": [
                    [ { "subnet": "10.244.3.0/24" } ]
                ]
            },
            "mtu": 1500
            
        },
        {
            "type": "portmap",
            "capabilities": {
                "portMappings": true
            }
        }
	]
}
```

## Pod IP addresses
```
% kc get pod -o wide                             
NAME                                READY   STATUS    RESTARTS   AGE   IP           NODE               NOMINATED NODE   READINESS GATES
nginx-deployment-84cbf74d95-2bg4b   1/1     Running   0          3s    10.244.1.3   mutinode-worker3   <none>           <none>


% kc get pod -n kube-system -o wide
NAME                                             READY   STATUS    RESTARTS   AGE     IP           NODE                     NOMINATED NODE   READINESS GATES
coredns-7d764666f9-5g8nx                         1/1     Running   0          7m10s   10.244.0.2   mutinode-control-plane   <none>           <none>
coredns-7d764666f9-dgmcg                         1/1     Running   0          7m10s   10.244.0.4   mutinode-control-plane   <none>           <none>
etcd-mutinode-control-plane                      1/1     Running   0          7m18s   172.18.0.5   mutinode-control-plane   <none>           <none>
kindnet-6gnm4                                    1/1     Running   0          7m7s    172.18.0.4   mutinode-worker          <none>           <none>
kindnet-6kpz4                                    1/1     Running   0          7m10s   172.18.0.5   mutinode-control-plane   <none>           <none>
kindnet-dz7zf                                    1/1     Running   0          7m7s    172.18.0.2   mutinode-worker2         <none>           <none>
kindnet-ts9wt                                    1/1     Running   0          7m7s    172.18.0.3   mutinode-worker3         <none>           <none>
kube-apiserver-mutinode-control-plane            1/1     Running   0          7m18s   172.18.0.5   mutinode-control-plane   <none>           <none>
kube-controller-manager-mutinode-control-plane   1/1     Running   0          7m17s   172.18.0.5   mutinode-control-plane   <none>           <none>
kube-proxy-7pxk4                                 1/1     Running   0          7m7s    172.18.0.3   mutinode-worker3         <none>           <none>
kube-proxy-c7lm5                                 1/1     Running   0          7m7s    172.18.0.4   mutinode-worker          <none>           <none>
kube-proxy-rt29n                                 1/1     Running   0          7m7s    172.18.0.2   mutinode-worker2         <none>           <none>
kube-proxy-sbmws                                 1/1     Running   0          7m10s   172.18.0.5   mutinode-control-plane   <none>           <none>
kube-scheduler-mutinode-control-plane            1/1     Running   0          7m18s   172.18.0.5   mutinode-control-plane   <none>           <none>
```

## Pod route
```
root@mutinode-worker2:/# ip route
default         via 172.18.0.1 dev eth0
10.244.0.0/24   via 172.18.0.5 dev eth0
10.244.1.0/24   via 172.18.0.3 dev eth0
10.244.2.0/24   via 172.18.0.4 dev eth0
172.18.0.0/16 dev eth0 proto kernel scope link src 172.18.0.2
```
