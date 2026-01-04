
```
% docker build -t nginx-with-ip .

% kubectl --context kind-my-kind apply -f /Users/i519210/SAPDevelop/mengxi-personal/learning-notes/k8s/kind/naive-sample/deploy-app/nginx-deployment.yaml
```

```
% kubectl --context kind-my-kind get pods -o wide 
NAME                                READY   STATUS    RESTARTS   AGE   IP           NODE                    NOMINATED NODE   READINESS GATES
nginx-deployment-59f86b59ff-twlj5   1/1     Running   0          54s   10.244.0.5   my-kind-control-plane   <none>           <none>


root@my-kind-control-plane:/# ip route show
default via 172.18.0.1 dev eth0 
10.244.0.2 dev veth1866ccc2 scope host 
10.244.0.3 dev vetha7b6163e scope host 
10.244.0.4 dev veth5f8bbed3 scope host 
10.244.0.5 dev veth510038b5 scope host 
172.18.0.0/16 dev eth0 proto kernel scope link src 172.18.0.2


root@my-kind-control-plane:/# ip address
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
7: veth510038b5@if4: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 12:e9:9e:a9:81:08 brd ff:ff:ff:ff:ff:ff link-netns cni-034c5efc-010a-c4e7-3cac-6c4fc1d96b3a
    inet 10.244.0.1/32 scope global veth510038b5
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