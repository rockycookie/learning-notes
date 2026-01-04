# K8s Kind
References:
- https://kind.sigs.k8s.io/docs/user/quick-start/

Interesting images:
- https://github.com/kubernetes-sigs/kind/tree/main/images/node
- https://github.com/kubernetes/ingress-nginx
- https://github.com/coredns/coredns/tree/master
- https://github.com/aojea/kindnet

## Instruction
```
go install sigs.k8s.io/kind@v0.31.0


% kind create cluster --name my-kind
Creating cluster "my-kind" ...
 ✓ Ensuring node image (kindest/node:v1.35.0) 🖼 
 ✓ Preparing nodes 📦  
 ✓ Writing configuration 📜 
 ✓ Starting control-plane 🕹️ 
 ✓ Installing CNI 🔌 
 ✓ Installing StorageClass 💾 
Set kubectl context to "kind-my-kind"


% docker ps --no-trunc
CONTAINER ID                                                       IMAGE                                                                                          COMMAND                                  CREATED         STATUS         PORTS                       NAMES
1a84d74bd95ec8adc9431c118f5f17e8569320378ade9c062bde00bd4a500762   kindest/node:v1.35.0@sha256:452d707d4862f52530247495d180205e029056831160e22870e37e3f6c1ac31f   "/usr/local/bin/entrypoint /sbin/init"   8 minutes ago   Up 8 minutes   127.0.0.1:51901->6443/tcp   my-kind-control-plane


% kubectl cluster-info --context kind-my-kind
Kubernetes control plane is running at https://127.0.0.1:51901
CoreDNS is running at https://127.0.0.1:51901/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
```
