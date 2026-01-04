## CNI (Container Network Interface)
Pod Creation: When a pod is scheduled, the kubelet asks the CNI plugin to set up networking for the pod
1. **Veth** Pair: The CNI plugin creates a virtual ethernet (veth) pair—one end in the pod’s network namespace, the other in the host’s
2. **Pod IP** Assignment: The plugin assigns an IP address to the pod (e.g., 10.244.x.x) from the cluster’s overlay network
3. **Routing**: Route is added for the IP to the veth. 
    - The host end of the veth is connected to a bridge or routed via tunnels (e.g., Flannel, Calico) so pods on different nodes can communicate.
4. **Network Policies**: Some CNI plugins enforce network policies for security and traffic control
