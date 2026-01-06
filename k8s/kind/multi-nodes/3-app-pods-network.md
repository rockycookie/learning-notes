# Network

## Pods IP
```
% kc get pod -o wide
NAME                                READY   STATUS    RESTARTS   AGE   IP           NODE               NOMINATED NODE   READINESS GATES
nginx-deployment-84cbf74d95-2bg4b   1/1     Running   0          32h   10.244.1.3   mutinode-worker3   <none>           <none>
nginx-deployment-84cbf74d95-5nkrp   1/1     Running   0          16s   10.244.2.3   mutinode-worker    <none>           <none>
nginx-deployment-84cbf74d95-8dcdh   1/1     Running   0          16s   10.244.3.2   mutinode-worker2   <none>           <none>
```

## ARP table (MAC route)

```
nginx-deployment-84cbf74d95-5nkrp:/# cat /sys/class/net/eth0/address 
b6:86:94:f6:69:5e

nginx-deployment-84cbf74d95-5nkrp:/# cat /proc/net/arp
IP address       HW type     Flags       HW address            Mask     Device
10.244.2.1       0x1         0x2         c6:0f:b5:24:98:69     *        eth0

nginx-deployment-84cbf74d95-5nkrp:/# arp -a
? (10.244.2.1) at c6:0f:b5:24:98:69 [ether]  on eth0
```

## IP route
- helper tool ref: https://codebeautify.org/hex-to-ip-converter
```
nginx-deployment-84cbf74d95-5nkrp:/# cat /proc/net/route
Iface	Destination	Gateway 	Flags	RefCnt	Use	Metric	Mask		MTU	Window	IRTT
eth0	00000000	0102F40A	0003	0	    0	0	    00000000	0	0	    0
eth0	0002F40A	0102F40A	0003	0	    0	0	    00FFFFFF	0	0       0
eth0	0102F40A	00000000	0005	0	    0	0	    FFFFFFFF	0	0	    0

nginx-deployment-84cbf74d95-5nkrp:/# ip route
default via 10.244.2.1 dev eth0
10.244.2.0/24 via 10.244.2.1 dev eth0 src 10.244.2.3
10.244.2.1 dev eth0 scope link src 10.244.2.3
```

## DNS server
```
nginx-deployment-84cbf74d95-5nkrp:/# cat /etc/resolv.conf
search default.svc.cluster.local svc.cluster.local cluster.local
nameserver 10.96.0.10
options ndots:5
```

## Traceroute
```
nginx-deployment-84cbf74d95-5nkrp:/# traceroute 10.244.1.3
traceroute to 10.244.1.3 (10.244.1.3), 30 hops max, 46 byte packets
 1  10.244.2.1 (10.244.2.1)  0.015 ms  0.012 ms  0.008 ms
 2  mutinode-worker3.kind (172.18.0.3)  0.007 ms  0.006 ms  0.009 ms
 3  10-244-1-3.nginx-service.default.svc.cluster.local (10.244.1.3)  0.493 ms  0.004 ms  0.006 ms


nginx-deployment-84cbf74d95-5nkrp:/# curl 10-244-1-3.nginx-service.default.svc.cluster.local -v
* Host 10-244-1-3.nginx-service.default.svc.cluster.local:80 was resolved.
* IPv6: (none)
* IPv4: 10.244.1.3
*   Trying 10.244.1.3:80...
* Established connection to 10-244-1-3.nginx-service.default.svc.cluster.local (10.244.1.3 port 80) from 10.244.2.3 port 36306 
* using HTTP/1.x
> GET / HTTP/1.1
> Host: 10-244-1-3.nginx-service.default.svc.cluster.local
> User-Agent: curl/8.17.0
> Accept: */*
> 
* Request completely sent off
< HTTP/1.1 200 OK
< Server: nginx/1.29.4
< Date: Mon, 05 Jan 2026 23:36:37 GMT
< Content-Type: text/html
< Content-Length: 615
< Last-Modified: Tue, 09 Dec 2025 19:40:55 GMT
< Connection: keep-alive
< ETag: "69387b47-267"
< Accept-Ranges: bytes
< 
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
html { color-scheme: light dark; }
body { width: 35em; margin: 0 auto;
font-family: Tahoma, Verdana, Arial, sans-serif; }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
* Connection #0 to host 10-244-1-3.nginx-service.default.svc.cluster.local:80 left intact
```
