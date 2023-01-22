# Concurancy Control

## Transaction
A unit of work performed by a DBMS against a database.
It supports failure recovery and concurrency isolation.

<br/>

## Read types:

- Dirty Read: a transaction retrieves a row that has been updated by another transaction that is not yet committed.
- Non-repeatable Read: a transaction retrieves a row twice and that row is updated by another transaction that is committed in between.
- Phantom Read: a transaction retrieves a set of rows twice and new rows are inserted into or removed from that set by another transaction that is committed in between.

<br/>

## Isolation Levels

### Serializable
- This is the highest isolation level
- Lock-based implementation
    - It requires 
        - **read and write locks** (acquired on selected data) to be released at the end of the transaction
        - **range-locks** when a SELECT query uses a ranged WHERE clause, especially to avoid the phantom reads phenomenon

### Snapshot Isolation
- If the system detects a write collision among several concurrent transactions, only one of them is allowed to commit
- Write skew (column value mixed by 2 transactions) is possible at this isolation level in some systems
- Read-only Transaction Skew

### Repeatable-read

- Lock-based implementation
    - **read and write locks** (acquired on selected data) to be released at the end of the transaction
    - No **range-locks**, so phantom reads phenomenon can occur
- Write skew (column value mixed by 2 transactions) is possible at this isolation level in some systems

### Read-committed

- Lock-based implementation
    - **write locks** (acquired on selected data) to be released at the end of the transaction
    - **read locks** (acquired on selected data) to be released as soon as the SELECT operation is performed, so non-repeatable reads phenomenon can occur
    - No **range-locks**, so phantom reads phenomenon can occur

### Read-uncommitted
Dirty reads are allowed...
