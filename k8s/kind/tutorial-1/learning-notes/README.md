## Open questions
- [x] Does ingress controller receives traffic? --> yes
    - I think it should be ingress who receives traffic? --> wrong
    - Why the controller has NodePort? -> exposing the port to accept traffic
        ```
        kubectl get svc --all-namespaces
        NAMESPACE       NAME                                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
        default         example-service                      ClusterIP   10.96.118.129   <none>        5678/TCP                     26h
        default         kubernetes                           ClusterIP   10.96.0.1       <none>        443/TCP                      26h
        ingress-nginx   ingress-nginx-controller             NodePort    10.96.123.189   <none>        80:30333/TCP,443:32452/TCP   26h
        ingress-nginx   ingress-nginx-controller-admission   ClusterIP   10.96.188.65    <none>        443/TCP                      26h
        kube-system     kube-dns                             ClusterIP   10.96.0.10      <none>        53/UDP,53/TCP,9153/TCP       26h
        ```
    - Nginx runs within the ingress-controller pod
        ```
        % kubectl -n ingress-nginx exec -it ingress-nginx-controller-d45d995d4-hm4fk -- bash

        ingress-nginx-controller-d45d995d4-hm4fk:/etc/nginx$ ps aux
        PID   USER     TIME  COMMAND
        1 www-data  0:00 /usr/bin/dumb-init -- /nginx-ingress-controller --election-id=ingress-nginx-leader --controller-class=k8s.io
        9 www-data  0:31 /nginx-ingress-controller --election-id=ingress-nginx-leader --controller-class=k8s.io/ingress-nginx --ingre
        25 www-data  0:00 nginx: master process /usr/bin/nginx -c /etc/nginx/nginx.conf
        888 www-data  0:00 nginx: worker process
        889 www-data  0:00 nginx: worker process
        890 www-data  0:00 nginx: worker process
        891 www-data  0:00 nginx: worker process
        892 www-data  0:00 nginx: worker process
        893 www-data  0:00 nginx: cache manager process
        1054 www-data  0:00 bash
        1062 www-data  0:00 ps aux
        ```
- [x] How ingress work with CoreDNS?
    - Nignx `resolver 10.96.0.10 valid=30s` point to the DNS server and dynamically resolve hosts when TTL expires
- [ ] Load balance on KinD
    - Learn from https://kccnceu2024.sched.com/event/1YhhY
