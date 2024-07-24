# Tutorial 1
Reference: 
- https://medium.com/@nash.checkin/kind-kubernetes-in-docker-beginners-guide-to-create-cluster-deployment-service-ingress-c4c819d82d82


## Steps

### Creating Cluster
```
kind create cluster --name t1-cluster --config config.yaml
```

then

```
% % docker ps
CONTAINER ID   IMAGE                  COMMAND                  CREATED          STATUS          PORTS                                                                 NAMES
701468a91918   kindest/node:v1.30.0   "/usr/local/bin/entr…"   18 seconds ago   Up 16 seconds   0.0.0.0:80->80/tcp, 0.0.0.0:443->443/tcp, 127.0.0.1:54765->6443/tcp   t1-cluster-control-plane

% kubectl --context kind-t1-cluster get pod -n kube-system
NAME                                               READY   STATUS    RESTARTS   AGE
coredns-7db6d8ff4d-8p999                           1/1     Running   0          12s
coredns-7db6d8ff4d-ps9tl                           1/1     Running   0          12s
etcd-t1-cluster-control-plane                      1/1     Running   0          28s
kindnet-wdlnc                                      1/1     Running   0          12s
kube-apiserver-t1-cluster-control-plane            1/1     Running   0          28s
kube-controller-manager-t1-cluster-control-plane   1/1     Running   0          28s
kube-proxy-69fdw                                   1/1     Running   0          12s
kube-scheduler-t1-cluster-control-plane            1/1     Running   0          28s
```

### Init Ingress
```
kubectl --context kind-t1-cluster apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
```

then

```
% kubectl --context kind-t1-cluster get pod -n ingress-nginx
NAME                                       READY   STATUS      RESTARTS   AGE
ingress-nginx-admission-create-ng5ll       0/1     Completed   0          2m22s
ingress-nginx-admission-patch-4pfrp        0/1     Completed   1          2m22s
ingress-nginx-controller-d45d995d4-hm4fk   1/1     Running     0          2m22s
```

### Apply other manifests

- `kubectl --context kind-t1-cluster apply -f k8s-manifests.yaml`

- Reachable by ClusterIP when calling from inside the cluster/container
```
% kubectl get svc
NAME              TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)    AGE
example-service   ClusterIP   10.96.118.129   <none>        5678/TCP   52m
kubernetes        ClusterIP   10.96.0.1       <none>        443/TCP    57m


% docker exec -it 701468a91918 bash
root@t1-cluster-control-plane:/# curl http://10.96.118.129:5678 -v
*   Trying 10.96.118.129:5678...
* Connected to 10.96.118.129 (10.96.118.129) port 5678 (#0)
> GET / HTTP/1.1
> Host: 10.96.118.129:5678
> User-Agent: curl/7.88.1
> Accept: */*
> 
< HTTP/1.1 200 OK
< X-App-Name: http-echo
< X-App-Version: 1.0.0
< Date: Wed, 24 Jul 2024 01:11:29 GMT
< Content-Length: 24
< Content-Type: text/plain; charset=utf-8
< 
'hello world!! from mz'
* Connection #0 to host 10.96.118.129 left intact
```

- Host `test.example.com` is not resolvable when calling from outside the cluster/container

```
% curl http://test.example.com:5678 --proxy http://localhost:80 -v
* Host localhost:80 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:80...
* Connected to localhost (::1) port 80
> GET http://test.example.com:5678/ HTTP/1.1
> Host: test.example.com:5678
> User-Agent: curl/8.6.0
> Accept: */*
> Proxy-Connection: Keep-Alive
> 
< HTTP/1.1 200 OK
< Date: Wed, 24 Jul 2024 01:26:26 GMT
< Content-Type: text/plain; charset=utf-8
< Content-Length: 24
< Connection: keep-alive
< X-App-Name: http-echo
< X-App-Version: 1.0.0
< 
'hello world!! from mz'
* Connection #0 to host localhost left intact
```
