
```bash
nginx-deployment-84cbf74d95-977h6:/# ip route
default via 10.244.0.1 dev eth0 
10.244.0.0/24 via 10.244.0.1 dev eth0 src 10.244.0.17 
10.244.0.1 dev eth0 scope link src 10.244.0.17



nginx-deployment-84cbf74d95-977h6:/# ip address
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host 
       valid_lft forever preferred_lft forever
2: tunl0@NONE: <NOARP> mtu 1480 qdisc noop state DOWN group default qlen 1000
    link/ipip 0.0.0.0 brd 0.0.0.0
3: ip6tnl0@NONE: <NOARP> mtu 1452 qdisc noop state DOWN group default qlen 1000
    link/tunnel6 :: brd :: permaddr 96bd:e8db:a50e::
4: eth0@if19: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
    link/ether 0e:0f:39:f2:94:91 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 10.244.0.17/24 brd 10.244.0.255 scope global eth0
       valid_lft forever preferred_lft forever
    inet6 fe80::c0f:39ff:fef2:9491/64 scope link 
       valid_lft forever preferred_lft forever
```
