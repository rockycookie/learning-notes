# Helloworld with Istio Gateway

### 03 - Istall my custom app with sidecar injection
```
% kind load docker-image nginx-with-ip:latest --name svc-mesh
Image: "nginx-with-ip:latest" with ID "sha256:ec61e9db3ce63b0dd80ab1657a78ea07b67785cb4f6ac8298e468446e48cab91" not yet present on node "svc-mesh-worker", loading...
Image: "nginx-with-ip:latest" with ID "sha256:ec61e9db3ce63b0dd80ab1657a78ea07b67785cb4f6ac8298e468446e48cab91" not yet present on node "svc-mesh-worker3", loading...
Image: "nginx-with-ip:latest" with ID "sha256:ec61e9db3ce63b0dd80ab1657a78ea07b67785cb4f6ac8298e468446e48cab91" not yet present on node "svc-mesh-control-plane", loading...
Image: "nginx-with-ip:latest" with ID "sha256:ec61e9db3ce63b0dd80ab1657a78ea07b67785cb4f6ac8298e468446e48cab91" not yet present on node "svc-mesh-control-plane2", loading...
Image: "nginx-with-ip:latest" with ID "sha256:ec61e9db3ce63b0dd80ab1657a78ea07b67785cb4f6ac8298e468446e48cab91" not yet present on node "svc-mesh-worker2", loading...


% kubectl apply -f ../../../my-nginx-app/my-nginx-app.yaml 
namespace/myns created
deployment.apps/my-nginx-app created

% kc get pod --all-namespaces -o wide
NAMESPACE   NAME                           READY   STATUS    RESTARTS   AGE   IP           NODE
default     my-nginx-app-84ff767b47-wxmts  2/2     Running   0          74s   10.244.2.6   svc-mesh-worker
```

#### How myapp routes to istio-proxy

```
my-nginx-app-6969fbb6df-p28zx:/# iptables -t nat -L

# Warning: iptables-legacy tables present, use iptables-legacy to see them
Chain PREROUTING (policy ACCEPT)
target     prot opt source               destination         
ISTIO_INBOUND  tcp  --  anywhere             anywhere            

Chain INPUT (policy ACCEPT)
target     prot opt source               destination         

Chain OUTPUT (policy ACCEPT)
target     prot opt source               destination         
ISTIO_OUTPUT  all  --  anywhere             anywhere            

Chain POSTROUTING (policy ACCEPT)
target     prot opt source               destination         

Chain ISTIO_INBOUND (1 references)
target     prot opt source               destination         
RETURN     tcp  --  anywhere             anywhere             tcp dpt:15008
RETURN     tcp  --  anywhere             anywhere             tcp dpt:15090
RETURN     tcp  --  anywhere             anywhere             tcp dpt:15021
RETURN     tcp  --  anywhere             anywhere             tcp dpt:15020
ISTIO_IN_REDIRECT  tcp  --  anywhere             anywhere            

Chain ISTIO_IN_REDIRECT (3 references)
target     prot opt source               destination         
REDIRECT   tcp  --  anywhere             anywhere             redir ports 15006

Chain ISTIO_OUTPUT (1 references)
target     prot opt source               destination         

RETURN     all  --  127.0.0.6            anywhere            
ISTIO_IN_REDIRECT  tcp  --  anywhere            !localhost            tcp dpt:!15008 owner UID match 1337
RETURN     all  --  anywhere             anywhere             ! owner UID match 1337
RETURN     all  --  anywhere             anywhere             owner UID match 1337
ISTIO_IN_REDIRECT  tcp  --  anywhere            !localhost            tcp dpt:!15008 owner GID match 1337
RETURN     all  --  anywhere             anywhere             ! owner GID match 1337
RETURN     all  --  anywhere             anywhere             owner GID match 1337
RETURN     all  --  anywhere             localhost           
ISTIO_REDIRECT  all  --  anywhere             anywhere            

Chain ISTIO_REDIRECT (1 references)
target     prot opt source               destination         
REDIRECT   tcp  --  anywhere             anywhere             redir ports 15001
```

Explains
--->
```
External request to 10.244.2.6:80 (nginx)
  → PREROUTING → ISTIO_INBOUND
  → Skip Istio admin ports
  → ISTIO_IN_REDIRECT → port 15006
  → istio-proxy inbound listener processes it
  → forwards to nginx:80


nginx (UID 0) makes request to external service
  ↓
OUTPUT chain
  ↓
ISTIO_OUTPUT chain
  ↓ 127.0.0.6? → skip
  ↓ UID 1337? → skip
  ↓ localhost? → skip
  ↓
ISTIO_REDIRECT chain --> REDIRECT → port 15001 (istio-proxy outbound listener)


┌─────────────────────────────────────────────────┐
│ Pod Network Namespace (shared by all containers)│
│                                                 │
│  ┌──────────┐         iptables NAT rules        │
│  │  myapp   │──────────────┐                    │
│  └──────────┘              ↓                    │
│                    ┌───────────────┐            │
│                    │ REDIRECT to   │            │
│  ┌──────────┐      │ istio-proxy   │            │
│  │istio-proxy      │ ports:        │            │
│  │port 15001│←─────┤ - 15001 (out) │            │
│  │port 15006│─────►│ - 15006 (in)  │            │
│  └────┬─────┘      └───────────────┘            │
│       ↓                                         │
│     Routing table                               │
│    (used AFTER iptables)                        │
│   10.244.2.1 via eth0                           │
└─────────────────────────────────────────────────┘
```

#### All containers in the same pod share the same Linux network namespace
```
% kubectl exec -it my-nginx-app-6969fbb6df-p28zx -c myapp -- ls -l /proc/self/ns    
total 0
lrwxrwxrwx    1 root     root             0 Feb  2 03:22 cgroup -> cgroup:[4026533959]
lrwxrwxrwx    1 root     root             0 Feb  2 03:22 ipc -> ipc:[4026533863]
lrwxrwxrwx    1 root     root             0 Feb  2 03:22 mnt -> mnt:[4026533957]
lrwxrwxrwx    1 root     root             0 Feb  2 03:22 net -> net:[4026533783]
lrwxrwxrwx    1 root     root             0 Feb  2 03:22 pid -> pid:[4026533958]
lrwxrwxrwx    1 root     root             0 Feb  2 03:22 pid_for_children -> pid:[4026533958]
lrwxrwxrwx    1 root     root             0 Feb  2 03:22 time -> time:[4026531834]
lrwxrwxrwx    1 root     root             0 Feb  2 03:22 time_for_children -> time:[4026531834]
lrwxrwxrwx    1 root     root             0 Feb  2 03:22 user -> user:[4026531837]
lrwxrwxrwx    1 root     root             0 Feb  2 03:22 uts -> uts:[4026533862]


% kubectl exec -it my-nginx-app-6969fbb6df-p28zx -c istio-proxy -- ls -l /proc/self/ns    
total 0
lrwxrwxrwx 1 istio-proxy istio-proxy 0 Feb  2 03:22 cgroup -> 'cgroup:[4026533867]'
lrwxrwxrwx 1 istio-proxy istio-proxy 0 Feb  2 03:22 ipc -> 'ipc:[4026533863]'
lrwxrwxrwx 1 istio-proxy istio-proxy 0 Feb  2 03:22 mnt -> 'mnt:[4026533865]'
lrwxrwxrwx 1 istio-proxy istio-proxy 0 Feb  2 03:22 net -> 'net:[4026533783]'
lrwxrwxrwx 1 istio-proxy istio-proxy 0 Feb  2 03:22 pid -> 'pid:[4026533866]'
lrwxrwxrwx 1 istio-proxy istio-proxy 0 Feb  2 03:22 pid_for_children -> 'pid:[4026533866]'
lrwxrwxrwx 1 istio-proxy istio-proxy 0 Feb  2 03:22 time -> 'time:[4026531834]'
lrwxrwxrwx 1 istio-proxy istio-proxy 0 Feb  2 03:22 time_for_children -> 'time:[4026531834]'
lrwxrwxrwx 1 istio-proxy istio-proxy 0 Feb  2 03:22 user -> 'user:[4026531837]'
lrwxrwxrwx 1 istio-proxy istio-proxy 0 Feb  2 03:22 uts -> 'uts:[4026533862]'
```

- All containers in the same pod share the exact same iptables rules because they're in the same network namespace. What's shared in a pod:
    - Network namespace (one per pod, not per container)
    - iptables rules
    - IP address
    - Network interfaces (eth0)
    - Routing tables
    - ARP cache

### 02 - Install app with sidecar injection
```
% kubectl label namespace default istio-injection=enabled
namespace/default labeled


% kc get namespaces --show-labels                        
NAME                 STATUS   AGE   LABELS
default              Active   34m   istio-injection=enabled,kubernetes.io/metadata.name=default

% kubectl apply -f helloworld.yaml                       
service/helloworld created
deployment.apps/helloworld-v1 created
deployment.apps/helloworld-v2 created

% kc exec -it helloworld-v1-696f8879d6-57pkn -c istio-proxy -- ip route
default via 10.244.2.1 dev eth0 
10.244.2.0/24 via 10.244.2.1 dev eth0 src 10.244.2.5 
10.244.2.1 dev eth0 scope link src 10.244.2.5

% kc get pod -o wide
NAME                             READY   STATUS    RESTARTS   AGE   IP           NODE
helloworld-v1-696f8879d6-57pkn   2/2     Running   0          17m   10.244.2.5   svc-mesh-worker

% kc exec -it helloworld-v1-696f8879d6-57pkn -c istio-proxy -- arp       
Address                  HWtype  HWaddress           Flags Mask            Iface
10.244.2.1               ether   02:c8:c8:2a:22:90   C                     eth0


% docker exec svc-mesh-worker ip addr
...
7: veth99f34a4c@if4: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:c8:c8:2a:22:90 brd ff:ff:ff:ff:ff:ff link-netns cni-4c52ee90-6dbe-d193-13f0-a20b9f8310a2
    inet 10.244.2.1/32 scope global veth99f34a4c
       valid_lft forever preferred_lft forever
37: eth0@if38: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:12:00:03 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.18.0.3/16 brd 172.18.255.255 scope global eth0
       valid_lft forever preferred_lft forever
    inet6 fc00:f853:ccd:e793::3/64 scope global nodad 
       valid_lft forever preferred_lft forever
    inet6 fe80::42:acff:fe12:3/64 scope link 
       valid_lft forever preferred_lft forever

% docker network inspect kind
"Name": "kind",
"Created": "2024-07-16T10:27:19.653539838Z",
"Scope": "local",
"Driver": "bridge",
"Subnet": "172.18.0.0/16",
"Gateway": "172.18.0.1",
"da08c...e7485": {
    "Name": "svc-mesh-worker",
    "MacAddress": "02:42:ac:12:00:03",
    "IPv4Address": "172.18.0.3/16"
}
```

### 01 - Install Istio
```
istio-nodes % ./../resources/istio-1.28.2/bin/istioctl install --set profile=demo -y
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

WARNING: Istio 1.26.0 may be out of support (EOL) already: see https://istio.io/latest/docs/releases/supported-releases/ for supported releases
✔ Istio core installed ⛵️
✔ Istiod installed 🧠
✔ Egress gateways installed 🛫
✔ Ingress gateways installed 🛬
✔ Installation complete
```

#### Notes
- For learning purpose, Istio resources are scheduled on one worker pod, which should not happen in production
┌─────────────────────────────────────┐
│ Dedicated Gateway/Edge Nodes        │
│ - istio-ingressgateway              │
│ - istio-egressgateway               │
│ - Node labels: node-role=gateway    │
│ - Higher network bandwidth          │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ Worker Nodes                        │
│ - Application pods + sidecars       │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ Control Plane Nodes                 │
│ - istiod                            │
│ - K8s control plane components      │
└─────────────────────────────────────┘


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
