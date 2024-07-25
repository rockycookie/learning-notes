## Open questions
- [ ] Does ingress controller receives traffic? I think it should be ingress who receives traffic
    - Why the controller has NodePort?
        ```
        kubectl get svc --all-namespaces
        NAMESPACE       NAME                                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
        default         example-service                      ClusterIP   10.96.118.129   <none>        5678/TCP                     26h
        default         kubernetes                           ClusterIP   10.96.0.1       <none>        443/TCP                      26h
        ingress-nginx   ingress-nginx-controller             NodePort    10.96.123.189   <none>        80:30333/TCP,443:32452/TCP   26h
        ingress-nginx   ingress-nginx-controller-admission   ClusterIP   10.96.188.65    <none>        443/TCP                      26h
        kube-system     kube-dns                             ClusterIP   10.96.0.10      <none>        53/UDP,53/TCP,9153/TCP       26h
        ```
    - Find evidence that ingress receives traffic
- [ ] How ingress work with CoreDNS?
