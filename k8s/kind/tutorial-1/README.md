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

Currently can not find the image; porbably need to:
1. Create an app image
2. `kind load docker-image example-docker-image:tag --name test-cluster`

```
% kubectl get pod                                                    
NAME                                  READY   STATUS             RESTARTS   AGE
example-deployment-86c859895c-57hhn   0/1     ImagePullBackOff   0          2m6s


% kubectl describe pod example-deployment-86c859895c-57hhn
Name:             example-deployment-86c859895c-57hhn
Namespace:        default
Priority:         0
Service Account:  default
Node:             t1-cluster-control-plane/172.18.0.2
Start Time:       Tue, 23 Jul 2024 17:19:19 -0700
Labels:           app=example-label
                  pod-template-hash=86c859895c
Annotations:      <none>
Status:           Pending
IP:               10.244.0.8
IPs:
  IP:           10.244.0.8
Controlled By:  ReplicaSet/example-deployment-86c859895c
Containers:
  example-container:
    Container ID:   
    Image:          example-image:tag
    Image ID:       
    Port:           8084/TCP
    Host Port:      0/TCP
    State:          Waiting
      Reason:       ImagePullBackOff
    Ready:          False
    Restart Count:  0
    Environment:
      DB_NAME:  <set to the key 'db.name' of config map 'example-config'>  Optional: false
      DB_SVC:   <set to the key 'db.svc' of config map 'example-config'>   Optional: false
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-mxl84 (ro)
Conditions:
  Type                        Status
  PodReadyToStartContainers   True 
  Initialized                 True 
  Ready                       False 
  ContainersReady             False 
  PodScheduled                True 
Volumes:
  kube-api-access-mxl84:
    Type:                    Projected (a volume that contains injected data from multiple sources)
    TokenExpirationSeconds:  3607
    ConfigMapName:           kube-root-ca.crt
    ConfigMapOptional:       <nil>
    DownwardAPI:             true
QoS Class:                   BestEffort
Node-Selectors:              <none>
Tolerations:                 node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
                             node.kubernetes.io/unreachable:NoExecute op=Exists for 300s
Events:
  Type     Reason     Age                  From               Message
  ----     ------     ----                 ----               -------
  Normal   Scheduled  2m20s                default-scheduler  Successfully assigned default/example-deployment-86c859895c-57hhn to t1-cluster-control-plane
  Normal   Pulling    47s (x4 over 2m20s)  kubelet            Pulling image "example-image:tag"
  Warning  Failed     46s (x4 over 2m19s)  kubelet            Failed to pull image "example-image:tag": failed to pull and unpack image "docker.io/library/example-image:tag": failed to resolve reference "docker.io/library/example-image:tag": pull access denied, repository does not exist or may require authorization: server message: insufficient_scope: authorization failed
  Warning  Failed     46s (x4 over 2m19s)  kubelet            Error: ErrImagePull
  Warning  Failed     32s (x6 over 2m19s)  kubelet            Error: ImagePullBackOff
  Normal   BackOff    20s (x7 over 2m19s)  kubelet            Back-off pulling image "example-image:tag"
```
