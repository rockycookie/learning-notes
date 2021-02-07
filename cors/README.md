## What is CORS

- From [Mozilla Doc](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS): "Cross-Origin Resource Sharing (CORS) is an HTTP-header based mechanism that allows a server to indicate any other origins (domain, scheme, or port) than its own from which a browser should permit loading of resources."

- From [Auth0 Blog](https://auth0.com/blog/cors-tutorial-a-guide-to-cross-origin-resource-sharing/): "Cross-Origin Resource Sharing (CORS) is a protocol that enables scripts running on a browser client to interact with resources from a different origin."

<br />

## Preflight request
Refer to [Auth0 Blog](https://auth0.com/blog/cors-tutorial-a-guide-to-cross-origin-resource-sharing/):

- Request:
    ```
    curl -i -X OPTIONS localhost:3001/api/ping \
    -H 'Access-Control-Request-Method: GET' \
    -H 'Access-Control-Request-Headers: Content-Type, Accept' \
    -H 'Origin: http://localhost:3000'
    ```
- Reponse:
    ```
    HTTP/1.1 204 No Content
    Access-Control-Allow-Origin: *
    Access-Control-Allow-Methods: GET,HEAD,PUT,PATCH,POST,DELETE
    Vary: Access-Control-Request-Headers
    Access-Control-Allow-Headers: Content-Type, Accept
    Content-Length: 0
    Date: Fri, 05 Apr 2019 11:41:08 GMT
    Connection: keep-alive
    ```
