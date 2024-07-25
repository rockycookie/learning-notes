# Load balance

There are 2 types:

## Internal load balancing
- typically among pods of the same application
- Pros
    - Distributing traffic evenly across pods to improve performance and reliability.
    - High availability ensures traffic can still be routed to pods even if some are unavailable.
    - Isolating traffic between different applications or services.
- a `ClusterIP` service type creates an internal load balancer that exposes the service to pods within the same cluster


## External load balancing
- distributing traffic from outside the Kubernetes cluster to appropriate pods within the cluster.
- could be done by
    - NodePort: expose a specific port on each node in the cluster
    - LoadBalancer: a service type that provisions an external load balancer, typically supplied by cloud providers
    - Ingress: a native Kubernetes resource that exposes routes
