## To spin up a PostgreSQL instance
```
docker run --name pg_learning -e POSTGRES_PASSWORD=root -d postgres:13.4-buster
```

Note:
- I am using an older version of Docker as below, which needs to work with `13.4-buster`
    ```
    $ docker version
    Server:
        Version:      17.03.1-ce-rc1
        API version:  1.27 (minimum version 1.12)
        Go version:   go1.7.5
        Git commit:   3476dbf
        Built:        Wed Mar 15 20:28:18 2017
        OS/Arch:      linux/amd64
        Experimental: true
    ```

<br/>

## To access the instance
```
# docker exec -it pg_learning bash

# psql -h localhost -p 5432 -U postgres
```
