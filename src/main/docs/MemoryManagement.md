# Spark memory tuning
Most of the content here is from: https://spark.apache.org/docs/latest/tuning.html

Memory usage in spark fall into two categories:
1. Execution memory
2. Storage memory

Execution memory refers to that used in computation for shuffles, joins, sorts and aggregation.
Storage memory refers to that used used caching and propagating internal data across cluster.

However, in spark they share a unified memory region (spark.memory.fraction). With no execution memory is used, storage 
can use the entire memory for storage. Execution may evict, storage if necessary, but only until storage memory falls 
under a certain threshold. This threshold is set with `spark.memory.storageFraction` (0.5 - default).


Default for memory region is 0.6. Rest of the space is reserved for user data structures, internal data structures and
safeguard against OOM errors.

How to find memory consumption: Storage tab in spark UI
To find the memory consumption of a particular object use SizeEstimator’s estimate method.