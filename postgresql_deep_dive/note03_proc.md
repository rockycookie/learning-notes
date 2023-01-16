## Process overview:
```
root@f538ce9c5fc7:/var/lib/postgresql/data/base/16384# ps -auxf | more
USER       PID %CPU %MEM    VSZ   RSS TTY      STAT START   TIME COMMAND
root       113  0.0  0.1   6860  3680 ?        Ss   21:01   0:00 bash
root      2855  0.0  0.4  19424  9140 ?        S+   21:12   0:00  \_ /usr/lib/postgresql/13/bin/psql -h localhost -p 5432 -U postgres
root        71  0.0  0.1   6988  3744 ?        Ss   20:40   0:00 bash
root      3647  0.0  0.1   7636  2712 ?        R+   23:58   0:00  \_ ps -auxf
root      3648  0.0  0.0   2496   900 ?        S+   23:58   0:00  \_ more
postgres     1  0.0  1.3 213916 26712 ?        Ss   20:39   0:01 postgres
postgres    65  0.0  0.3 214032  7992 ?        Ss   20:39   0:00 postgres: checkpointer
postgres    66  0.0  0.2 213916  5784 ?        Ss   20:39   0:00 postgres: background writer
postgres    67  0.0  0.4 213916 10000 ?        Ss   20:39   0:00 postgres: walwriter
postgres    68  0.0  0.4 214464  8804 ?        Ss   20:39   0:00 postgres: autovacuum launcher
postgres    69  0.0  0.2  68412  4808 ?        Ss   20:39   0:00 postgres: stats collector
postgres    70  0.0  0.3 214348  6776 ?        Ss   20:39   0:00 postgres: logical replication launcher
postgres  2897  0.0  1.0 217148 20892 ?        Ss   21:29   0:00 postgres: postgres mydb ::1(52332) idle


root@f538ce9c5fc7:/var/lib/postgresql/data/base/16384# pstree -sp 1
postgres(1)-+-postgres(65)
            |-postgres(66)
            |-postgres(67)
            |-postgres(68)
            |-postgres(69)
            |-postgres(70)
            `-postgres(2897)
```

