# Transaction Log
- Record of change & action history
- WAL(Write Ahead Log) / Xlog - Recover data when system crashes
    - PITR(Point-in-time Recovery)
    - SR(Streaming Replication)

<br/>

## How it works in general
- **How recording is done:**
    - When an action like INSERT, DELETE, UPDATE happens, PgSQL writes XLog into WAL buffer in memory. 
    - And then when the transaction is commited, PgSQL writes the WAL buffer into WAL segment file (persistent memory).
- **Which records should be applied for recovery:** Checkpoint record process running in background, which
    - is executed periodically
    - each time it writes a XLog (Checkpoint Record) into WAL, which indicates the REDO point
- **What if OS write fails, when the REDO point matches most recent XLog:** Full-page write, when enabled
    - Each time after inserting checkpoint record, write the entire page and its headers as a XLog (also called backup block or full-page image)

