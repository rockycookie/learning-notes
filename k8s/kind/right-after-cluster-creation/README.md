## Create cluster and export logs
```
% kind create cluster --name mg-cluster --config config.yaml
Creating cluster "mg-cluster" ...
 ✓ Ensuring node image (kindest/node:v1.30.0) 🖼 
 ✓ Preparing nodes 📦 📦 📦 📦  
 ✓ Writing configuration 📜 
 ✓ Starting control-plane 🕹️ 
 ✓ Installing CNI 🔌 
 ✓ Installing StorageClass 💾 
 ✓ Joining worker nodes 🚜 
Set kubectl context to "kind-mg-cluster"
You can now use your cluster with:

kubectl cluster-info --context kind-mg-cluster

Thanks for using kind! 😊

% kind export logs --name mg-cluster
Exporting logs for cluster "mg-cluster" to:
/private/var/folders/5n/sq3v0rkx4vqg777qj9txld9c0000gp/T/1009948535

% kubectl cluster-info --context kind-mg-cluster
Kubernetes control plane is running at https://127.0.0.1:53077
CoreDNS is running at https://127.0.0.1:53077/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

## docker observation
```
% docker ps
CONTAINER ID   IMAGE                  COMMAND                  CREATED          STATUS          PORTS                       NAMES
96876c692dd8   kindest/node:v1.30.0   "/usr/local/bin/entr…"   41 minutes ago   Up 41 minutes                               mg-cluster-worker2
873c20cc395c   kindest/node:v1.30.0   "/usr/local/bin/entr…"   41 minutes ago   Up 41 minutes                               mg-cluster-worker
bd2add0e3466   kindest/node:v1.30.0   "/usr/local/bin/entr…"   41 minutes ago   Up 41 minutes   127.0.0.1:53077->6443/tcp   mg-cluster-control-plane
dc0ea7908842   kindest/node:v1.30.0   "/usr/local/bin/entr…"   41 minutes ago   Up 41 minutes                               mg-cluster-worker3
```

## kubectl observation
```
% kubectl --context kind-mg-cluster get pod -n kube-system
NAME                                               READY   STATUS    RESTARTS   AGE
coredns-7db6d8ff4d-b5pbp                           1/1     Running   0          20m
coredns-7db6d8ff4d-tqpgq                           1/1     Running   0          20m
etcd-mg-cluster-control-plane                      1/1     Running   0          20m
kindnet-6twlv                                      1/1     Running   0          20m
kindnet-bwfhf                                      1/1     Running   0          20m
kindnet-gc29f                                      1/1     Running   0          20m
kindnet-v92hj                                      1/1     Running   0          20m
kube-apiserver-mg-cluster-control-plane            1/1     Running   0          20m
kube-controller-manager-mg-cluster-control-plane   1/1     Running   0          20m
kube-proxy-777ck                                   1/1     Running   0          20m
kube-proxy-mt8ml                                   1/1     Running   0          20m
kube-proxy-p4szr                                   1/1     Running   0          20m
kube-proxy-vn9xb                                   1/1     Running   0          20m
kube-scheduler-mg-cluster-control-plane            1/1     Running   0          20m

% kubectl --context kind-mg-cluster get pod -n local-path-storage
NAME                                    READY   STATUS    RESTARTS   AGE
local-path-provisioner-988d74bc-799t9   1/1     Running   0          23m
```
