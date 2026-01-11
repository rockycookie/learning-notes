# Kubectl

- To list containers inside a pod
    ```
    get pod bookinfo-gateway-istio-5f76f5688b-76bk8 -o jsonpath="{.spec.containers[*].name}"
    ```
- To list routes inside a httproute
    ```
    % kubectl get httproute bookinfo -o jsonpath="{.spec.rules[*].matches[*].path}"

    {"type":"Exact","value":"/productpage"} {"type":"PathPrefix","value":"/static"} {"type":"Exact","value":"/login"} {"type":"Exact","value":"/logout"} {"type":"PathPrefix","value":"/api/v1/products"}
    ```
