# Deployment Replicas

After setting to 3

```
% kubectl get pod
NAME                                  READY   STATUS    RESTARTS   AGE
example-deployment-6456ff94b9-kpswl   1/1     Running   0          30h
example-deployment-6456ff94b9-qtlpw   1/1     Running   0          5m44s
example-deployment-6456ff94b9-wrjrd   1/1     Running   0          5m44s
```

When sending many requests, by repeating
```
curl http://test2.example.com:5678/mztest --proxy http://localhost:80

* Host localhost:80 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:80...
* Connected to localhost (::1) port 80
> GET http://test2.example.com:5678/mztest HTTP/1.1
> Host: test2.example.com:5678
> User-Agent: curl/8.6.0
> Accept: */*
> Proxy-Connection: Keep-Alive
> 
< HTTP/1.1 200 OK
< Date: Thu, 25 Jul 2024 07:42:20 GMT
< Content-Type: text/plain; charset=utf-8
< Content-Length: 24
< Connection: keep-alive
< X-App-Name: http-echo
< X-App-Version: 1.0.0
< 
'hello world!! from mz'
* Connection #0 to host localhost left intact
```


each pod handles some of the requests

```
% kubectl logs example-deployment-6456ff94b9-qtlpw
2024/07/25 07:37:59 [INFO] server is listening on :5678
2024/07/25 07:41:07 test2.example.com:5678 10.244.0.7:43382 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 13.5µs
2024/07/25 07:42:13 test2.example.com:5678 10.244.0.7:43954 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 12.25µs
2024/07/25 07:42:14 test2.example.com:5678 10.244.0.7:43970 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 5.209µs
2024/07/25 07:42:15 test2.example.com:5678 10.244.0.7:43978 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 4.583µs
2024/07/25 07:42:17 test2.example.com:5678 10.244.0.7:44020 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 4.875µs
2024/07/25 07:42:19 test2.example.com:5678 10.244.0.7:43970 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 4.708µs
i519210@GMXT6L4TL4 tutorial-1 % kubectl logs example-deployment-6456ff94b9-wrjrd
2024/07/25 07:37:59 [INFO] server is listening on :5678
2024/07/25 07:41:05 test2.example.com:5678 10.244.0.7:47336 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 14.084µs
2024/07/25 07:42:14 test2.example.com:5678 10.244.0.7:47948 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 5.042µs
2024/07/25 07:42:16 test2.example.com:5678 10.244.0.7:47972 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 9.958µs
2024/07/25 07:42:17 test2.example.com:5678 10.244.0.7:47998 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 8.041µs
2024/07/25 07:42:20 test2.example.com:5678 10.244.0.7:47972 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 4.667µs
i519210@GMXT6L4TL4 tutorial-1 % kubectl logs example-deployment-6456ff94b9-kpswl
2024/07/24 01:10:11 [INFO] server is listening on :5678
2024/07/24 01:11:29 10.96.118.129:5678 10.244.0.1:41642 "GET / HTTP/1.1" 200 24 "curl/7.88.1" 20.625µs
2024/07/24 01:26:12 test.example.com:5678 10.244.0.7:49474 "GET / HTTP/1.1" 200 24 "curl/8.6.0" 7µs
2024/07/24 01:26:26 test.example.com:5678 10.244.0.7:49592 "GET / HTTP/1.1" 200 24 "curl/8.6.0" 5.334µs
2024/07/25 07:13:53 test.example.com:5678 10.244.0.7:50768 "GET / HTTP/1.1" 200 24 "curl/8.6.0" 26.5µs
2024/07/25 07:14:10 test2.example.com:5678 10.244.0.7:50768 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 54.375µs
2024/07/25 07:38:45 test2.example.com:5678 10.244.0.7:35262 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 4.791µs
2024/07/25 07:39:50 test2.example.com:5678 10.244.0.7:35828 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 5.958µs
2024/07/25 07:41:04 test2.example.com:5678 10.244.0.7:36458 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 5.666µs
2024/07/25 07:41:06 test2.example.com:5678 10.244.0.7:36478 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 5.666µs
2024/07/25 07:42:16 test2.example.com:5678 10.244.0.7:37122 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 4.833µs
2024/07/25 07:42:18 test2.example.com:5678 10.244.0.7:37152 "GET /mztest HTTP/1.1" 200 24 "curl/8.6.0" 4.959µs
```
