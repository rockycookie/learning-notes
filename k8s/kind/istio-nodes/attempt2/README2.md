# Another try

### 03 - Apply customized app with sidecar injection
```
% kind load docker-image nginx-with-ip:latest --name svc-mesh
Image: "nginx-with-ip:latest" with ID "sha256:ec61e9db3ce63b0dd80ab1657a78ea07b67785cb4f6ac8298e468446e48cab91" not yet present on node "svc-mesh-worker2", loading...
Image: "nginx-with-ip:latest" with ID "sha256:ec61e9db3ce63b0dd80ab1657a78ea07b67785cb4f6ac8298e468446e48cab91" not yet present on node "svc-mesh-worker", loading...
Image: "nginx-with-ip:latest" with ID "sha256:ec61e9db3ce63b0dd80ab1657a78ea07b67785cb4f6ac8298e468446e48cab91" not yet present on node "svc-mesh-worker3", loading...
Image: "nginx-with-ip:latest" with ID "sha256:ec61e9db3ce63b0dd80ab1657a78ea07b67785cb4f6ac8298e468446e48cab91" not yet present on node "svc-mesh-control-plane", loading...
Image: "nginx-with-ip:latest" with ID "sha256:ec61e9db3ce63b0dd80ab1657a78ea07b67785cb4f6ac8298e468446e48cab91" not yet present on node "svc-mesh-control-plane2", loading...

% kc -n myns apply -f ./resources/my-nginx-app/my-nginx-app.yaml
namespace/myns created
deployment.apps/my-nginx-app created

% istioctl kube-inject \
  --injectConfigFile inject-config.yaml \
  --meshConfigFile mesh-config.yaml \
  --valuesFile inject-values.yaml \
  --filename ../../resources/my-nginx-app/my-nginx-app.yaml \
  | kubectl apply -f -
namespace/myns unchanged
deployment.apps/my-nginx-app configured
```

Verification

```
% kc -n myns get pod
NAME                            READY   STATUS    RESTARTS   AGE
my-nginx-app-54c95548d4-ptvwr   2/2     Running   0          29s

% kc -n myns get pod my-nginx-app-54c95548d4-ptvwr -o jsonpath="{.spec.containers[*].name}"
myapp istio-proxy
```

### 02 - Apply k8s resources with manual sidecar injection
```
% istioctl kube-inject -f samples/curl/curl.yaml | kubectl apply -f -
serviceaccount/curl created
service/curl created
deployment.apps/curl created

% kubectl -n istio-system get configmap istio-sidecar-injector -o=jsonpath='{.data.config}' > inject-config.yaml
% kubectl -n istio-system get configmap istio-sidecar-injector -o=jsonpath='{.data.values}' > inject-values.yaml
% kubectl -n istio-system get configmap istio -o=jsonpath='{.data.mesh}' > mesh-config.yaml

% istioctl kube-inject \
  --injectConfigFile ../../istio-nodes/attempt2/inject-config.yaml \
  --meshConfigFile ../../istio-nodes/attempt2/mesh-config.yaml \
  --valuesFile ../../istio-nodes/attempt2/inject-values.yaml \
  --filename samples/curl/curl.yaml \
  | kubectl apply -f -
serviceaccount/curl unchanged
service/curl unchanged
deployment.apps/curl configured
```
- `istioctl kube-inject`: Reads a Kubernetes YAML (here, samples/curl/curl.yaml) and injects the Istio sidecar container (istio-proxy) into the pod spec.
- `--injectConfigFile`: Specifies a custom sidecar injection template (inject-config.yaml), controlling how the sidecar is added.
  - Resources (CPU/memory) for the sidecar.
  - Init containers for network setup.
  - Container image, arguments, probes, and environment variables for istio-proxy.
  - Volumes and volume mounts for certificates, tokens, and other data.
  - Metadata (labels, annotations) added to injected pods.
- `--meshConfigFile`: Supplies mesh-wide configuration (mesh-config.yaml), such as telemetry, tracing, and trust domain settings.
- `--valuesFile`: Provides Helm-style values (inject-values.yaml) to parameterize the injection (e.g., resource limits, logging, proxy settings).
- `--filename`: The input manifest to inject (samples/curl/curl.yaml).
- `| kubectl apply -f -`: Pipes the modified manifest (with sidecar injected) directly to kubectl, which applies it to the cluster.


Verfication

```
% kc get pod curl-b6d7c7544-77lgk -o jsonpath="{.spec.containers[*].name}"
curl istio-proxy
```

### 01 - Install istio
```
% istioctl install -f samples/bookinfo/demo-profile-no-gateways.yaml -y
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
✔ Istiod installed 🧠
✔ Installation complete
```

### 00 - Create kind cluster
```
% kind create cluster --config kind-config.yaml --name svc-mesh
Creating cluster "svc-mesh" ...
 ✓ Ensuring node image (kindest/node:v1.35.0) 🖼 
 ✓ Preparing nodes 📦 📦 📦 📦 📦  
 ✓ Configuring the external load balancer ⚖️ 
 ✓ Writing configuration 📜 
 ✓ Starting control-plane 🕹️ 
 ✓ Installing CNI 🔌 
 ✓ Installing StorageClass 💾 
 ✓ Joining more control-plane nodes 🎮 
 ✓ Joining worker nodes 🚜 
Set kubectl context to "kind-svc-mesh"
You can now use your cluster with:

kubectl cluster-info --context kind-svc-mesh

Have a nice day! 👋

% kc cluster-info --context kind-svc-mesh
Kubernetes control plane is running at https://127.0.0.1:63605
CoreDNS is running at https://127.0.0.1:63605/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```
