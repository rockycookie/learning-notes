# Within a single node - Index

- An additional data struct that is derived from the primary data
- Performance tradeoff: **Query Pattern matters!**
    - Write: extra overhead; appending to a file is the simpleast
    - Read: speed up

<br/>

## How to write to disk
Comparing:
- Sequential writes
    - Appending (sequential writes) and segment merging (write merged to new and delete previous files)
        - Faster
        - Concurrency and crash recovery are much simpler
        - Avoids data file fragmentation (deleting old files)
- Random writes

<br/>

## Hash Table + Sequential Writes
### Scenario, KV-pair storage: frequent write, small number of (small-sized) keys
- Examples:
    - key might be the URL of a video, and the value might be the number of times it has been played
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

### How it works

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
    1. append a special deletion record to the data file
    2. When log segments are merged, the tombstone tells the merging process to discard any previous values for the deleted key

<br/>

### Limitations
- The hash table must fit in memory
- Range queries are not efficient


<br/><br/>


## SSTable (Sorted String Table) or LSM-Tree (Log-Structure Merge Tree)
Balanced Tree + Sequential Writes

### How it works:
- How to insert:
    1. Add the key-value pair into an in-memory balanced search tree data strcuture
    2. When the in-memory tree grows larger than certain threshold, write it into disk as a SSTable file
        - Sequential write to disk as the keys are sorted already
        - The threshold is typically a few MB
        - The new SSTable file becomes the most reecent segment of the db
        - When SSTable is being written to disk, new writes go to a new in-memory tree
    3. Background thread/proc merging segments via MergeSort ALG
        - We still need an in-memory structure to record disk memory offset, but now it can be sparse
            - one key for every few kilobytes of segment file is sufficient
        - Combine segment files and discard overwritten or deleted values
            - Create new merged sorted segnment file
                1. Read segment files (which are sorted already) side-by-side
                2. Copy the lowest key to the new file
                3. Repeat
                4. When multiple segments have the same key, pick the one in the most recent segment

- How to read
    1. Search the key in the in-memory balanced search tree
    2. Then in the most recent on-disk segment and then the next most recent segment

<br/>

### Pros & Cons
- Pros
    - Same memory supports more keys (not every key has to be stored in memory)
    - Merging segments is simple and efficient, even if the files are bigger than the available memory
    - Small number of high-frequency writes are mostly in-memory, **FAST for writes**
        - Suitable for pattern where we have big number of keys but in a time window, high-frequency writes only go to small number of keys
- Cons
    -  When crashes, the most recent writes (which are in the memtable but not yet written out to disk) are lost.
        - We can keep a **separate log** on disk to which every write is immediately appended, More Disk Write
    - To confirm a key does not exist in the database, we need to look up each segment tree (a lot of disk writes)
        - A **bloom filter** is a memory-efficient data structure for approximating the contents of a set. It can tell you if a key does not exists
appear in the database


<br/><br/>



