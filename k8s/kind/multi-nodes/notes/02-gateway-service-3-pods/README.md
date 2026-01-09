# Gateway + Service + 3 Pods
a Gateway is typically required by a service mesh (like Istio, Linkerd, etc.) to manage ingress and egress traffic between the mesh and external networks.
- Managing external traffic (ingress/egress) with advanced routing, TLS termination, and load balancing
- Enabling features like path-based routing, traffic splitting, and API security
- Integrating with service mesh policies for observability, authentication, and authorization
- Providing a single entry/exit point for traffic between the cluster and external networks

Different from Service
- Service = basic internal/external access and load balancing
- Gateway = advanced traffic control, security, and integration for complex scenarios

## Apply the new k8s objects
1. Need to apply the CRD
    - Download yaml file from `https://github.com/kubernetes-sigs/gateway-api/releases/tag/v1.4.1`
    ```
    kubectl --context kind-mutinode apply -f /<workspace>/mengxi-personal/learning-notes/k8s/kind/multi-nodes/notes/02-gateway-service-3-pods/gateway-api1.4.1-standard-install.yaml

    customresourcedefinition.apiextensions.k8s.io/backendtlspolicies.gateway.networking.k8s.io created
    customresourcedefinition.apiextensions.k8s.io/gatewayclasses.gateway.networking.k8s.io created
    customresourcedefinition.apiextensions.k8s.io/gateways.gateway.networking.k8s.io created
    customresourcedefinition.apiextensions.k8s.io/grpcroutes.gateway.networking.k8s.io created
    customresourcedefinition.apiextensions.k8s.io/httproutes.gateway.networking.k8s.io created
    customresourcedefinition.apiextensions.k8s.io/referencegrants.gateway.networking.k8s.io created
    ```
2. Apply the manifest
    ```
    kubectl --context kind-mutinode apply -f /<workspace>/mengxi-personal/learning-notes/k8s/kind/multi-nodes/deploy-app/nginx-deployment.yaml

    deployment.apps/nginx-deployment unchanged
    service/nginx-service unchanged
    gateway.gateway.networking.k8s.io/nginx-gateway created
    ```

## TLS Termination

To generate a TLS certificate for Kubernetes, you can use OpenSSL. Here’s a simple way:

1. Generate a private key: `openssl genrsa -out tls.key 2048`
2. Generate a certificate signing request (CSR): `openssl req -new -key tls.key -out tls.csr -subj "/CN=nginx.example.com"`
3. Generate a self-signed certificate: `openssl x509 -req -in tls.csr -signkey tls.key -out tls.crt -days 365`
4. Create a Kubernetes TLS secret: `kubectl create secret tls nginx-tls-secret --cert=tls.crt --key=tls.key`
