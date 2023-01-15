## Environment variables & Root repository
```
# env | grep PG
PG_MAJOR=13
PG_VERSION=13.4-1.pgdg100+1
PGDATA=/var/lib/postgresql/data

# ls $PGDATA
base	      pg_dynshmem    pg_logical    pg_replslot	 pg_stat      pg_tblspc    pg_wal		 postgresql.conf
global	      pg_hba.conf    pg_multixact  pg_serial	 pg_stat_tmp  pg_twophase  pg_xact		 postmaster.opts
pg_commit_ts  pg_ident.conf  pg_notify	   pg_snapshots  pg_subtrans  PG_VERSION   postgresql.auto.conf  postmaster.pid

# ls $PGDATA/base
1  13394  13395
```

<br/>

## The base repo
- Each repo under `$PGDATA/base` is a database, their repo names are their `oid`
- Tables will be stored as files with the name of their `oid`
    - Find `oid` of table `accounts`
        ```
        mydb=# SELECT relname, oid, relfilenode FROM pg_class WHERE relname = 'accounts';
        accounts | 16408 |       16408
        ```
    - Show the file
        ```
        root@f538ce9c5fc7:/var/lib/postgresql/data/base/16384# ls -l 16408
        -rw------- 1 postgres postgres 0 Jan 15 21:37 16408
        ```

<br/>
