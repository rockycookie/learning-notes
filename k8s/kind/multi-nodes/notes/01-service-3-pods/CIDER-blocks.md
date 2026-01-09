# The CIDER blocks
- The `172.18.0.0/16` subnet is typically used as the **Docker bridge network**, which is often assigned to the control plane and worker nodes for inter-node communication
    - for control-plane and node-to-node communication
- The `10.244.0.0/16` network is called the **pod network** (or pod CIDR). It is the **overlay network** used by Kubernetes to assign IP addresses to pods, enabling pod-to-pod communication across nodes in the cluster. Each node gets a subnet (e.g., 10.244.1.0/24, 10.244.2.0/24) from this range for its pods
- The `10.96.0.0/16` network
    - default ClusterIP range
    - to assign virtual IPs to services (like kube-dns, nginx-service)
- `192.168.65.2` is set as the DNS nameserver for containers/nodes. It is not an IP assigned to a node or pod, but rather the address of the DNS gateway provided by Docker Desktop (or your container runtime) for **external name resolution**

## All Pod info
```
% kc get pod -o wide --all-namespaces
NAMESPACE            NAME                                             READY   STATUS    RESTARTS   AGE     IP           NODE                     NOMINATED NODE   READINESS GATES
default              nginx-deployment-84cbf74d95-2bg4b                1/1     Running   0          4d12h   10.244.1.3   mutinode-worker3         <none>           <none>
default              nginx-deployment-84cbf74d95-5nkrp                1/1     Running   0          3d3h    10.244.2.3   mutinode-worker          <none>           <none>
default              nginx-deployment-84cbf74d95-8dcdh                1/1     Running   0          3d3h    10.244.3.2   mutinode-worker2         <none>           <none>
kube-system          coredns-7d764666f9-5g8nx                         1/1     Running   0          4d12h   10.244.0.2   mutinode-control-plane   <none>           <none>
kube-system          coredns-7d764666f9-dgmcg                         1/1     Running   0          4d12h   10.244.0.4   mutinode-control-plane   <none>           <none>
kube-system          etcd-mutinode-control-plane                      1/1     Running   0          4d12h   172.18.0.5   mutinode-control-plane   <none>           <none>
kube-system          kindnet-6gnm4                                    1/1     Running   0          4d12h   172.18.0.4   mutinode-worker          <none>           <none>
kube-system          kindnet-6kpz4                                    1/1     Running   0          4d12h   172.18.0.5   mutinode-control-plane   <none>           <none>
kube-system          kindnet-dz7zf                                    1/1     Running   0          4d12h   172.18.0.2   mutinode-worker2         <none>           <none>
kube-system          kindnet-ts9wt                                    1/1     Running   0          4d12h   172.18.0.3   mutinode-worker3         <none>           <none>
kube-system          kube-apiserver-mutinode-control-plane            1/1     Running   0          4d12h   172.18.0.5   mutinode-control-plane   <none>           <none>
kube-system          kube-controller-manager-mutinode-control-plane   1/1     Running   0          4d12h   172.18.0.5   mutinode-control-plane   <none>           <none>
kube-system          kube-proxy-7pxk4                                 1/1     Running   0          4d12h   172.18.0.3   mutinode-worker3         <none>           <none>
kube-system          kube-proxy-c7lm5                                 1/1     Running   0          4d12h   172.18.0.4   mutinode-worker          <none>           <none>
kube-system          kube-proxy-rt29n                                 1/1     Running   0          4d12h   172.18.0.2   mutinode-worker2         <none>           <none>
kube-system          kube-proxy-sbmws                                 1/1     Running   0          4d12h   172.18.0.5   mutinode-control-plane   <none>           <none>
kube-system          kube-scheduler-mutinode-control-plane            1/1     Running   0          4d12h   172.18.0.5   mutinode-control-plane   <none>           <none>
local-path-storage   local-path-provisioner-67b8995b4b-dsqlg          1/1     Running   0          4d12h   10.244.0.3   mutinode-control-plane   <none>           <none>
```

## Pod terminal
```
% kc get pod -o wide
NAME                                READY   STATUS    RESTARTS   AGE     IP           NODE               NOMINATED NODE   READINESS GATES
nginx-deployment-84cbf74d95-2bg4b   1/1     Running   0          4d11h   10.244.1.3   mutinode-worker3   <none>           <none>
nginx-deployment-84cbf74d95-5nkrp   1/1     Running   0          3d2h    10.244.2.3   mutinode-worker    <none>           <none>
nginx-deployment-84cbf74d95-8dcdh   1/1     Running   0          3d2h    10.244.3.2   mutinode-worker2   <none>           <none>


nginx-deployment-84cbf74d95-2bg4b:/# traceroute 10.244.1.3
traceroute to 10.244.1.3 (10.244.1.3), 30 hops max, 46 byte packets
 1  nginx-deployment-84cbf74d95-2bg4b (10.244.1.3)  0.014 ms  0.006 ms  0.004 ms


nginx-deployment-84cbf74d95-2bg4b:/# traceroute 10.244.3.2
traceroute to 10.244.3.2 (10.244.3.2), 30 hops max, 46 byte packets
 1  10.244.1.1 (10.244.1.1)  0.005 ms  0.004 ms  0.003 ms
 2  mutinode-worker2.kind (172.18.0.2)  0.002 ms  0.004 ms  0.003 ms
 3  10-244-3-2.nginx-service.default.svc.cluster.local (10.244.3.2)  0.004 ms  0.003 ms  0.003 ms
```

## Node terminal
```
root@mutinode-worker2:/# ip address
26: eth0@if27: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:12:00:02 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.18.0.2/16 brd 172.18.255.255 scope global eth0


root@mutinode-worker2:/# ip route
default via 172.18.0.1 dev eth0 
10.244.0.0/24 via 172.18.0.5 dev eth0 
10.244.1.0/24 via 172.18.0.3 dev eth0 
10.244.2.0/24 via 172.18.0.4 dev eth0 
10.244.3.2 dev vethc34f9b6f scope host 
172.18.0.0/16 dev eth0 proto kernel scope link src 172.18.0.2
```

### All nodes
`docker network inspect kind` would also give this information and also `"Gateway": "172.18.0.1"`

```
root@mutinode-worker:/# ip address
172.18.0.4/16

root@mutinode-worker2:/# ip address
172.18.0.2/16

root@mutinode-worker3:/# ip address
172.18.0.3/16

root@mutinode-control-plane:/# ip address
172.18.0.5/16
```

### Node ARP table
```
root@mutinode-worker2:/# cat /proc/net/arp
IP address       HW type     Flags       HW address            Mask     Device
172.18.0.3       0x1         0x2         02:42:ac:12:00:03     *        eth0
172.18.0.4       0x1         0x2         02:42:ac:12:00:04     *        eth0
10.244.3.2       0x1         0x2         02:3d:1e:ed:99:7d     *        vethc34f9b6f
172.18.0.5       0x1         0x2         02:42:ac:12:00:05     *        eth0
172.18.0.1       0x1         0x2         02:42:ec:51:d7:cc     *        eth0
```

### Node local DNS
```
root@mutinode-worker2:/# cat /etc/resolv.conf
nameserver 192.168.65.2
options ndots:0
```
