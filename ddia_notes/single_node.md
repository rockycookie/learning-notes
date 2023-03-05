# Within a single node

<br/>

## Scenario, KV-pair storage: frequent write, small number of (small-sized) keys
- In-memory hash map, mapped to file-byte-offset
- New value append to a file on the disk
- Restriction:
    - All the keys fit in the available RAM (values are just stored in on disk thus not affecting RAM)
