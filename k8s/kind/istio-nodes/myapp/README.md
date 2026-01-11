# My Application

## Steps
### 01 - Load docker image to kind
```
my-nginx-app % docker build -t nginx-with-ip:latest .

% kind load docker-image nginx-with-ip:latest --name istio-nodes
```

### 02 - Apply the deployment to the cluster
```
% kc -n myns apply -f my-nginx-app.yaml
namespace/myns created
deployment.apps/my-nginx-app created

% kc get pod -n myns -o wide
NAME                            READY   STATUS    RESTARTS   AGE   IP           NODE
my-nginx-app-84ff767b47-fcknb   1/1     Running   0          80s   10.244.3.5   istio-nodes-worker2
```

## Testing
```
% kc get pod -o wide
NAME                                      READY   STATUS    RESTARTS   AGE    IP           NODE
bookinfo-gateway-istio-5f76f5688b-76bk8   1/1     Running   0          142m   10.244.4.4   istio-nodes-worker
details-v1-6cc9f5cc44-kk25q               2/2     Running   0          159m   10.244.2.3   istio-nodes-worker3
productpage-v1-7f885b46fc-pwqrg           2/2     Running   0          159m   10.244.2.4   istio-nodes-worker3
ratings-v1-77b8b6df5b-ngc87               2/2     Running   0          159m   10.244.3.2   istio-nodes-worker2
reviews-v1-fdbf79cd8-vzvp8                2/2     Running   0          159m   10.244.4.2   istio-nodes-worker
reviews-v2-674c6d8b4-slgxs                2/2     Running   0          159m   10.244.4.3   istio-nodes-worker
reviews-v3-7b775c7568-fdwsk               2/2     Running   0          159m   10.244.3.3   istio-nodes-worker2
```
