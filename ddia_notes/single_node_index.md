# Within a single node - Index

- An additional data struct that is derived from the primary data
- Performance tradeoff: **Query Pattern matters!**
    - Write: extra overhead; appending to a file is the simpleast
    - Read: speed up

<br/>

## How to write to disk
Comparing:
- Appending and segment merging
    - Faster
    - Concurrency and crash recovery are much simpler
    - Avoids data file fragmentation (deleting old files)
- Random writes

<br/>

## Hash Table Index
### Scenario, KV-pair storage: frequent write, small number of (small-sized) keys
- Examples:
    - key might be the URL of a cat video, and the value might be the number of times it has been played
        - in this kind of workload, there are a lot of writes, but there are NOT too many distinct keys
- In-memory hash map, mapped to file-byte-offset
- New value append to a file on the disk
- Restriction:
    - All the keys fit in the available RAM (values are just stored in on disk thus not affecting RAM)

<br/>

### The implementation
To avoid running out of disk space,
we break the single file into segments of a certain size by
    - closing a segment file when it reaches a certain size
    - making subsequent writes to a new segment file

It’s faster and simpler to use a binary format
that first encodes the length of a string in bytes, followed by the raw string.

Crash recovery
- Take snapshots of the hash tables and store them on disk; and load them after crash
- Use checksums, allowing corrupted parts of the segments to be detected and ignored

<br/>

How it works

- How to insert: 
    1. Simply appending to the most recent segment file
    2. Update the in-memory hash map of the segment file to point to the segment's byte-offset of the value
        - yes, each segment has its own in-memory hash map
    3. background threads will do:
        - compaction (making one segment smaller by throwing away duplicate keys in the segment) 
        - segment merging (write merged content into new files and delete the old files)

- How to read:
    1. In memory, check the most recent segment's hash map, then the second-most-recent segment's hash map, and so on, until we find the key
    2. Use the value read from the hash map (segment file byte-offset) to fetch the actual data from the disk

- How to delete:
    1. append a special deletion record to the data file(sometimes called a tombstone)
    2. When log segments are merged, the tombstone tells the merging process to discard any previous values for the deleted key

<br/>

### Limitations
- The hash table must fit in memory
- Range queries are not efficient