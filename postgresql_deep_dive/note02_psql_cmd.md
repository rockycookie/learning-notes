## List users
```
postgres=# \du
                                   List of roles
 Role name |                         Attributes                         | Member of 
-----------+------------------------------------------------------------+-----------
 postgres  | Superuser, Create role, Create DB, Replication, Bypass RLS | {}
```

<br/>

## List databases
```
postgres=# \l+

                                                                   List of databases
   Name    |  Owner   | Encoding |  Collate   |   Ctype    |   Access privileges   |  Size   | Tablespace |                Description                 
-----------+----------+----------+------------+------------+-----------------------+---------+------------+--------------------------------------------
 mydb      | postgres | UTF8     | en_US.utf8 | en_US.utf8 |                       | 7877 kB | pg_default | 
 postgres  | postgres | UTF8     | en_US.utf8 | en_US.utf8 |                       | 7877 kB | pg_default | default administrative connection database
 template0 | postgres | UTF8     | en_US.utf8 | en_US.utf8 | =c/postgres          +| 7729 kB | pg_default | unmodifiable empty database
           |          |          |            |            | postgres=CTc/postgres |         |            | 
 template1 | postgres | UTF8     | en_US.utf8 | en_US.utf8 | =c/postgres          +| 7729 kB | pg_default | default template for new databases
           |          |          |            |            | postgres=CTc/postgres |         |            | 
(4 rows)
```

<br/>

## Connect to a database
```
postgres=# \c mydb
psql (13.9 (Debian 13.9-1.pgdg100+1), server 13.4 (Debian 13.4-1.pgdg100+1))
You are now connected to database "mydb" as user "postgres".
mydb=#
```

<br/>

## List tables, views, schemas
```
mydb=# CREATE TABLE accounts (
user_id serial PRIMARY KEY,
username VARCHAR ( 50 ) UNIQUE NOT NULL,
password VARCHAR ( 50 ) NOT NULL,
email VARCHAR ( 255 ) UNIQUE NOT NULL,
created_on TIMESTAMP NOT NULL,
        last_login TIMESTAMP 
);
CREATE TABLE

mydb=# \d
 public | accounts             | table    | postgres
 public | accounts_user_id_seq | sequence | postgres
```
