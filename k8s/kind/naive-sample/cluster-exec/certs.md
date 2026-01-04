
## Kubernetes cluster’s Certificate Authority (API Server)

`root@my-kind-control-plane:/etc/kubernetes# cat /etc/kubernetes/pki/ca.crt` gets the same public key for API server

```bash
openssl x509 -in cluster-exec/api-server-pub.pem -noout -text

Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number: 5556585456729489843 (0x4d1cf508687681b3)
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: CN=kubernetes
        Validity
            Not Before: Jan  4 10:22:02 2026 GMT
            Not After : Jan  2 10:27:02 2036 GMT
        Subject: CN=kubernetes
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    ...
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Key Usage: critical
                Digital Signature, Key Encipherment, Certificate Sign
            X509v3 Basic Constraints: critical
                CA:TRUE
            X509v3 Subject Key Identifier: 
                99:EB:62:DB:1F:5A:17:FE:95:AC:D6:A3:2B:E1:E9:7A:7A:FD:9A:55
            X509v3 Subject Alternative Name: 
                DNS:kubernetes
    Signature Algorithm: sha256WithRSAEncryption
    Signature Value:
        ...
```

## Scheduler

```bash
% openssl x509 -in cluster-exec/scheduler-pub.pem -noout -text 
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number: 5509410300381480019 (0x4c755b7a641dc453)
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: CN=kubernetes
        Validity
            Not Before: Jan  4 10:22:02 2026 GMT
            Not After : Jan  4 10:27:02 2027 GMT
        Subject: CN=system:kube-scheduler
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    ...
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Key Usage: critical
                Digital Signature, Key Encipherment
            X509v3 Extended Key Usage: 
                TLS Web Client Authentication
            X509v3 Basic Constraints: critical
                CA:FALSE
            X509v3 Authority Key Identifier: 
                99:EB:62:DB:1F:5A:17:FE:95:AC:D6:A3:2B:E1:E9:7A:7A:FD:9A:55
    Signature Algorithm: sha256WithRSAEncryption
    Signature Value:
        ...
```

## Controller Manager
```bash
% openssl x509 -in cluster-exec/controller-manager/controller-manager-pub.pem -noout -text 
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number: 3390409399134076402 (0x2f0d2762a6883df2)
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: CN=kubernetes
        Validity
            Not Before: Jan  4 10:22:02 2026 GMT
            Not After : Jan  4 10:27:02 2027 GMT
        Subject: CN=system:kube-controller-manager
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    ...
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Key Usage: critical
                Digital Signature, Key Encipherment
            X509v3 Extended Key Usage: 
                TLS Web Client Authentication
            X509v3 Basic Constraints: critical
                CA:FALSE
            X509v3 Authority Key Identifier: 
                99:EB:62:DB:1F:5A:17:FE:95:AC:D6:A3:2B:E1:E9:7A:7A:FD:9A:55
    Signature Algorithm: sha256WithRSAEncryption
    Signature Value:
        ...
```

## Kubelet
- `/var/lib/kubelet/pki/kubelet-client-current.pem`
```bash
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number: 5984023817119060051 (0x530b85f4fadbd053)
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: CN=kubernetes
        Validity
            Not Before: Jan  4 10:22:02 2026 GMT
            Not After : Jan  4 10:27:02 2027 GMT
        Subject: O=system:nodes, CN=system:node:my-kind-control-plane
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    ...
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Key Usage: critical
                Digital Signature, Key Encipherment
            X509v3 Extended Key Usage: 
                TLS Web Client Authentication
            X509v3 Basic Constraints: critical
                CA:FALSE
            X509v3 Authority Key Identifier: 
                99:EB:62:DB:1F:5A:17:FE:95:AC:D6:A3:2B:E1:E9:7A:7A:FD:9A:55
    Signature Algorithm: sha256WithRSAEncryption
    Signature Value:
        ...
```