# Ingress
- https://github.com/kubernetes/ingress-nginx
- https://kubernetes.github.io/ingress-nginx/how-it-works/

## Relevant K8s resources

### Ingress resource
```
% kubectl get ingress               
NAME              CLASS   HOSTS              ADDRESS     PORTS   AGE
example-ingress   nginx   test.example.com   localhost   80      26h

% kubectl describe ing example-ingress
Name:             example-ingress
Labels:           <none>
Namespace:        default
Address:          localhost
Ingress Class:    nginx
Default backend:  <default>
Rules:
  Host              Path  Backends
  ----              ----  --------
  test.example.com  
                    /   example-service:5678 (10.244.0.18:5678)
Annotations:        nginx.ingress.kubernetes.io/ssl-redirect: false
Events:             <none>
```

### Ingress controller
```
% kubectl -n ingress-nginx get pod    
NAME                                       READY   STATUS      RESTARTS   AGE
ingress-nginx-admission-create-ng5ll       0/1     Completed   0          26h
ingress-nginx-admission-patch-4pfrp        0/1     Completed   1          26h
ingress-nginx-controller-d45d995d4-hm4fk   1/1     Running     0          26h

% kubectl -n ingress-nginx get svc
NAME                                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                      AGE
ingress-nginx-controller             NodePort    10.96.123.189   <none>        80:30333/TCP,443:32452/TCP   26h
ingress-nginx-controller-admission   ClusterIP   10.96.188.65    <none>        443/TCP                      26h
```

### Check Nginx Config

```
% kubectl -n ingress-nginx exec -it ingress-nginx-controller-d45d995d4-hm4fk -- cat /etc/nginx/nginx.conf

...
	## start server test.example.com
	server {
		server_name test.example.com ;
		
		http2 on;
		
		listen 80  ;
		listen [::]:80  ;
		listen 443  ssl;
		listen [::]:443  ssl;
		
		set $proxy_upstream_name "-";
		
		ssl_certificate_by_lua_block {
			certificate.call()
		}
		
		location / {
			
			set $namespace      "default";
			set $ingress_name   "example-ingress";
			set $service_name   "example-service";
			set $service_port   "5678";
			set $location_path  "/";
			set $global_rate_limit_exceeding n;
			
			rewrite_by_lua_block {
				lua_ingress.rewrite({
					force_ssl_redirect = false,
					ssl_redirect = false,
					force_no_ssl_redirect = false,
					preserve_trailing_slash = false,
					use_port_in_redirects = false,
					global_throttle = { namespace = "", limit = 0, window_size = 0, key = { }, ignored_cidrs = { } },
				})
				balancer.rewrite()
				plugins.run()
			}
			
			# be careful with `access_by_lua_block` and `satisfy any` directives as satisfy any
			# will always succeed when there's `access_by_lua_block` that does not have any lua code doing `ngx.exit(ngx.DECLINED)`
			# other authentication method such as basic auth or external auth useless - all requests will be allowed.
			#access_by_lua_block {
			#}
			
			header_filter_by_lua_block {
				lua_ingress.header()
				plugins.run()
			}
			
			body_filter_by_lua_block {
				plugins.run()
			}
			
			log_by_lua_block {
				balancer.log()
				
				plugins.run()
			}
			
			port_in_redirect off;
			
			set $balancer_ewma_score -1;
			set $proxy_upstream_name "default-example-service-5678";
			set $proxy_host          $proxy_upstream_name;
			set $pass_access_scheme  $scheme;
			
			set $pass_server_port    $server_port;
			
			set $best_http_host      $http_host;
			set $pass_port           $pass_server_port;
			
			set $proxy_alternative_upstream_name "";
			
			client_max_body_size                    1m;
			
			proxy_set_header Host                   $best_http_host;
			
			# Pass the extracted client certificate to the backend
			
			# Allow websocket connections
			proxy_set_header                        Upgrade           $http_upgrade;
			
			proxy_set_header                        Connection        $connection_upgrade;
			
			proxy_set_header X-Request-ID           $req_id;
			proxy_set_header X-Real-IP              $remote_addr;
			
			proxy_set_header X-Forwarded-For        $remote_addr;
			
			proxy_set_header X-Forwarded-Host       $best_http_host;
			proxy_set_header X-Forwarded-Port       $pass_port;
			proxy_set_header X-Forwarded-Proto      $pass_access_scheme;
			proxy_set_header X-Forwarded-Scheme     $pass_access_scheme;
			
			proxy_set_header X-Scheme               $pass_access_scheme;
			
			# Pass the original X-Forwarded-For
			proxy_set_header X-Original-Forwarded-For $http_x_forwarded_for;
			
			# mitigate HTTPoxy Vulnerability
			# https://www.nginx.com/blog/mitigating-the-httpoxy-vulnerability-with-nginx/
			proxy_set_header Proxy                  "";
			
			# Custom headers to proxied server
			
			proxy_connect_timeout                   5s;
			proxy_send_timeout                      60s;
			proxy_read_timeout                      60s;
			
			proxy_buffering                         off;
			proxy_buffer_size                       4k;
			proxy_buffers                           4 4k;
			
			proxy_max_temp_file_size                1024m;
			
			proxy_request_buffering                 on;
			proxy_http_version                      1.1;
			
			proxy_cookie_domain                     off;
			proxy_cookie_path                       off;
			
			# In case of errors try the next upstream server before returning an error
			proxy_next_upstream                     error timeout;
			proxy_next_upstream_timeout             0;
			proxy_next_upstream_tries               3;
			
			# Custom Response Headers
			
			proxy_pass http://upstream_balancer;
			
			proxy_redirect                          off;
			
		}
		
	}
	## end server test.example.com
...
```
