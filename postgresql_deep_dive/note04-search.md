# Query Logic

In general, every request connection is handled by/mapped to a background process.
A backgrounf process contains:

1. Parser - Parse tree generator (translate SQL cmd)
    - Example: `SELECT ...` ==> `struct SelectStmt defined at parsenodes.h`
2. Analyzer - Query tree generator (analyze parse tree)
    - Example: `struct SelectStmt` ==> `struct Query defined at parsenodes.h`
3. Rewriter - Query tree overwriter (overwrite query tree based on system rules)
    - Example: mofidy `struct Query`
4. Planner - Plan tree generator (find the most efficient from the query tree)
    - Example: `struct Query` ==> `struct PlannedStmt defined at plannodes.h`
    - `EXPLAIN ${SQL CMD}` will display the plan tree of the SQL command
    - About cost
        - `costsize.c` defines methods of cots calculation, e.g.: `cost_seqscan()`, `cost_index()`
        - Types of cost:
            - startup cost (to reach the first page/block of any possible relation file)
            - execution cost (to find all expected results)
            - total cost (sum of the two above)
        - Cost will be displayed by `EXPLAIN` cmd
5. Executor - (execute the plan tree)

<br/>

## To Add: How is the cost estimation done for sequence scan, index scan and sort operation
