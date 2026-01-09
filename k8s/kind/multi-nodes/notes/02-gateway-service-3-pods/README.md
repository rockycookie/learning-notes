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
3. After which the Gateway k8s object is created, but no Gateway Controler picks up this object and provision the real gateway (address is empty and programmed is unknown)
    ```
    % kc get gateway
    NAME            CLASS                 ADDRESS   PROGRAMMED   AGE
    nginx-gateway   nginx-gateway-class             Unknown      4h23m
    ```
4. Download Gateway Controller, Istio
    - https://istio.io/latest/docs/setup/getting-started
    ```
    % curl -L https://istio.io/downloadIstio | sh -
    cd istio-*
    % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                    Dload  Upload   Total   Spent    Left  Speed
    100   102  100   102    0     0    159      0 --:--:-- --:--:-- --:--:--   159
    100  5124  100  5124    0     0   6167      0 --:--:-- --:--:-- --:--:--  6167

    Downloading istio-1.28.2 from https://github.com/istio/istio/releases/download/1.28.2/istio-1.28.2-osx-arm64.tar.gz ...

    Istio 1.28.2 download complete!

    The Istio release archive has been downloaded to the istio-1.28.2 directory.

    To configure the istioctl client tool for your workstation,
    add the istio-1.28.2/bin directory to your environment path variable with:
        export PATH=$PWD/bin:$PATH

    Begin the Istio pre-installation check by running:
        istioctl x precheck 

    Try Istio in ambient mode
        https://istio.io/latest/docs/ambient/getting-started/
    Try Istio in sidecar mode
        https://istio.io/latest/docs/setup/getting-started/
    Install guides for ambient mode
        https://istio.io/latest/docs/ambient/install/
    Install guides for sidecar mode
        https://istio.io/latest/docs/setup/install/

    Need more information? Visit https://istio.io/latest/docs/
    ```
5. Install and Uninstall
    - Got some failure starting ingress/egress
        ```% kc get -n istio-system pod
        NAME                                    READY   STATUS             RESTARTS      AGE
        istio-egressgateway-67cf7cbcdd-bcm2b    0/1     CrashLoopBackOff   7 (27s ago)   12m
        istio-ingressgateway-6bcdbb9678-pbhbb   0/1     CrashLoopBackOff   7 (66s ago)   12m
        istiod-666f895d5d-pvkhs                 1/1     Running            0             12m
        ```
    ```
    istioctl install --set profile=demo -y
    ...

    istioctl uninstall --purge -y
    ...
    ```
    - Got it working without installing ingress/egress
        ```
        istioctl install -f samples/bookinfo/demo-profile-no-gateways.yaml -y
        ```

## TLS Termination

To generate a TLS certificate for Kubernetes, you can use OpenSSL. Here’s a simple way:

1. Generate a private key: `openssl genrsa -out tls.key 2048`
2. Generate a certificate signing request (CSR): `openssl req -new -key tls.key -out tls.csr -subj "/CN=nginx.example.com"`
3. Generate a self-signed certificate: `openssl x509 -req -in tls.csr -signkey tls.key -out tls.crt -days 365`
4. Create a Kubernetes TLS secret: `kubectl create secret tls nginx-tls-secret --cert=tls.crt --key=tls.key`
