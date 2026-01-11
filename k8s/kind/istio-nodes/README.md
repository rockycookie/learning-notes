# Istio Application Sample 1
- https://istio.io/latest/docs/setup/getting-started

## Notes
### Problem: No space left on device
- Issue Description
    ```
    Warning  Failed     40s                 kubelet            Failed to pull image "docker.io/istio/examples-bookinfo-details-v1:1.20.3": failed to pull and unpack image "docker.io/istio/examples-bookinfo-details-v1:1.20.3": failed to extract layer (application/vnd.oci.image.layer.v1.tar+gzip sha256:a7010733b9410cfe1be9a9ab045cd6c89448c583fd9b9a1c1312dd950f855490) to overlayfs as "extract-378468252-LDwG sha256:080757cd9b1895987b4cc611173f21e6f165b7e60bc69712b346c05eeb083bac": write /var/lib/containerd/io.containerd.snapshotter.v1.overlayfs/snapshots/31/fs/usr/local/lib/ruby/3.4.0/aarch64-linux/enc/trans/emoji_sjis_docomo.so: no space left on device
    ```
- Fix
    - Delete unused Docker images: `docker image prune -a`
    - Remove unused containers: `docker container prune`
    - Clean up unused volumes: `docker volume prune`
    - Clean up unused networks: `docker network prune`

## 07 - Access the application
```
% kc port-forward svc/bookinfo-gateway-istio 8080:80
Forwarding from 127.0.0.1:8080 -> 80
Forwarding from [::1]:8080 -> 80
```
Then web is running on browser `http://localhost:8080/productpage`

## 06 - Open the application to outside traffic
```
% kc apply -f samples/bookinfo/gateway-api/bookinfo-gateway.yaml
gateway.gateway.networking.k8s.io/bookinfo-gateway created
httproute.gateway.networking.k8s.io/bookinfo created

% kc get gateway --all-namespaces --show-labels
NAMESPACE   NAME               CLASS   ADDRESS   PROGRAMMED   AGE     LABELS
default     bookinfo-gateway   istio             False        3m14s   <none>
```

By default, Istio creates a LoadBalancer service for a gateway. As we will access this gateway by a tunnel, we don’t need a load balancer. If you want to learn about how load balancers are configured for external IP addresses, read the ingress gateways documentation.

```
% kc annotate gateway bookinfo-gateway networking.istio.io/service-type=ClusterIP --namespace=default
gateway.gateway.networking.k8s.io/bookinfo-gateway annotated

% kc get gateway --all-namespaces --show-labels                                                      
NAMESPACE   NAME               CLASS   ADDRESS                                            PROGRAMMED   AGE     LABELS
default     bookinfo-gateway   istio   bookinfo-gateway-istio.default.svc.cluster.local   True         8m45s   <none>
```

## 05 - Deploy the sample application
```
istio-1.28.2 % kc apply -f samples/bookinfo/platform/kube/bookinfo.yaml
service/details created
serviceaccount/bookinfo-details created
deployment.apps/details-v1 created
service/ratings created
serviceaccount/bookinfo-ratings created
deployment.apps/ratings-v1 created
service/reviews created
serviceaccount/bookinfo-reviews created
deployment.apps/reviews-v1 created
deployment.apps/reviews-v2 created
deployment.apps/reviews-v3 created
service/productpage created
serviceaccount/bookinfo-productpage created
deployment.apps/productpage-v1 created

% kc get pod -o wide                 
NAME                              READY   STATUS            RESTARTS   AGE   IP           NODE                  NOMINATED NODE   READINESS GATES
details-v1-6cc9f5cc44-kk25q       1/2     PodInitializing   0          94s   10.244.2.3   istio-nodes-worker3   <none>           <none>
productpage-v1-7f885b46fc-pwqrg   1/2     PodInitializing   0          94s   10.244.2.4   istio-nodes-worker3   <none>           <none>
ratings-v1-77b8b6df5b-ngc87       1/2     PodInitializing   0          94s   10.244.3.2   istio-nodes-worker2   <none>           <none>
reviews-v1-fdbf79cd8-vzvp8        1/2     PodInitializing   0          94s   10.244.4.2   istio-nodes-worker    <none>           <none>
reviews-v2-674c6d8b4-slgxs        1/2     PodInitializing   0          94s   10.244.4.3   istio-nodes-worker    <none>           <none>
reviews-v3-7b775c7568-fdwsk       1/2     PodInitializing   0          94s   10.244.3.3   istio-nodes-worker2   <none>           <none>

% kc get service -o wide
NAME          TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)    AGE   SELECTOR
details       ClusterIP   10.96.194.232   <none>        9080/TCP   10m   app=details
kubernetes    ClusterIP   10.96.0.1       <none>        443/TCP    34m   <none>
productpage   ClusterIP   10.96.179.250   <none>        9080/TCP   10m   app=productpage
ratings       ClusterIP   10.96.145.175   <none>        9080/TCP   10m   app=ratings
reviews       ClusterIP   10.96.31.241    <none>        9080/TCP   10m   app=reviews
```

## 04 - Install Kubernetes Gateway API CRDs
```
% kubectl get crd gateways.gateway.networking.k8s.io &> /dev/null || \
{ kubectl kustomize "github.com/kubernetes-sigs/gateway-api/config/crd?ref=v1.4.0" | kubectl apply -f -; }

customresourcedefinition.apiextensions.k8s.io/backendtlspolicies.gateway.networking.k8s.io created
customresourcedefinition.apiextensions.k8s.io/gatewayclasses.gateway.networking.k8s.io created
customresourcedefinition.apiextensions.k8s.io/gateways.gateway.networking.k8s.io created
customresourcedefinition.apiextensions.k8s.io/grpcroutes.gateway.networking.k8s.io created
customresourcedefinition.apiextensions.k8s.io/httproutes.gateway.networking.k8s.io created
customresourcedefinition.apiextensions.k8s.io/referencegrants.gateway.networking.k8s.io created


% kc get crd --sort-by=.metadata.creationTimestamp
NAME                                           CREATED AT
telemetries.telemetry.istio.io                 2026-01-10T23:47:49Z
serviceentries.networking.istio.io             2026-01-10T23:47:49Z
destinationrules.networking.istio.io           2026-01-10T23:47:49Z
envoyfilters.networking.istio.io               2026-01-10T23:47:49Z
workloadentries.networking.istio.io            2026-01-10T23:47:49Z
peerauthentications.security.istio.io          2026-01-10T23:47:49Z
gateways.networking.istio.io                   2026-01-10T23:47:49Z
proxyconfigs.networking.istio.io               2026-01-10T23:47:49Z
workloadgroups.networking.istio.io             2026-01-10T23:47:49Z
wasmplugins.extensions.istio.io                2026-01-10T23:47:49Z
virtualservices.networking.istio.io            2026-01-10T23:47:49Z
authorizationpolicies.security.istio.io        2026-01-10T23:47:49Z
requestauthentications.security.istio.io       2026-01-10T23:47:49Z
sidecars.networking.istio.io                   2026-01-10T23:47:49Z

backendtlspolicies.gateway.networking.k8s.io   2026-01-10T23:58:14Z
referencegrants.gateway.networking.k8s.io      2026-01-10T23:58:14Z
grpcroutes.gateway.networking.k8s.io           2026-01-10T23:58:14Z
gateways.gateway.networking.k8s.io             2026-01-10T23:58:14Z
gatewayclasses.gateway.networking.k8s.io       2026-01-10T23:58:14Z
httproutes.gateway.networking.k8s.io           2026-01-10T23:58:14Z
```

## 03 - label namespace for Envoy sidecar injection during application deployment
```
% kc label namespace default istio-injection=enabled

% kc get namespaces --show-labels
NAME                 STATUS   AGE    LABELS
default              Active   16m    istio-injection=enabled,kubernetes.io/metadata.name=default
istio-system         Active   7m5s   kubernetes.io/metadata.name=istio-system
kube-node-lease      Active   16m    kubernetes.io/metadata.name=kube-node-lease
kube-public          Active   16m    kubernetes.io/metadata.name=kube-public
kube-system          Active   16m    kubernetes.io/metadata.name=kube-system
local-path-storage   Active   16m    kubernetes.io/metadata.name=local-path-storage
```

## 02 - Install Istio
```
istio-1.28.2 % export PATH=$PWD/bin:$PATH
istio-1.28.2 % istioctl install -f samples/bookinfo/demo-profile-no-gateways.yaml -y
        |\          
        | \         
        |  \        
        |   \       
      /||    \      
     / ||     \     
    /  ||      \    
   /   ||       \   
  /    ||        \  
 /     ||         \ 
/______||__________\
____________________
  \__       _____/  
     \_____/        

✔ Istio core installed ⛵️
✔ Istiod installed
✔ Installation complete


% kc get namespaces --show-labels
NAME                 STATUS   AGE     LABELS
default              Active   14m     kubernetes.io/metadata.name=default
istio-system         Active   5m20s   kubernetes.io/metadata.name=istio-system
kube-node-lease      Active   14m     kubernetes.io/metadata.name=kube-node-lease
kube-public          Active   14m     kubernetes.io/metadata.name=kube-public
kube-system          Active   14m     kubernetes.io/metadata.name=kube-system
local-path-storage   Active   14m     kubernetes.io/metadata.name=local-path-storage
```


## 01 - Start new cluster

### Kill old cluster
```
% kind get clusters
mutinode

% kind delete cluster --name mutinode 
Deleting cluster "mutinode" ...
Deleted nodes: ["mutinode-worker2" "mutinode-worker3" "mutinode-worker" "mutinode-control-plane"]
```

### Start new cluster
```
% kind create cluster --config kind-config.yaml --name istio-nodes
Creating cluster "istio-nodes" ...
 ✓ Ensuring node image (kindest/node:v1.35.0) 🖼 
 ✓ Preparing nodes 📦 📦 📦 📦 📦  
 ✓ Configuring the external load balancer ⚖️ 
 ✓ Writing configuration 📜 
 ✓ Starting control-plane 🕹️ 
 ✓ Installing CNI 🔌 
 ✓ Installing StorageClass 💾 
 ✓ Joining more control-plane nodes 🎮 
 ✓ Joining worker nodes 🚜 
Set kubectl context to "kind-istio-nodes"

% kubectl config get-contexts
CURRENT   NAME              CLUSTER             AUTHINFO            NAMESPACE
*         kind-istio-nodes  kind-istio-nodes    kind-istio-nodes
```
