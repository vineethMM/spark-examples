# Memory tuning in Spark
Most of the content here is from: https://spark.apache.org/docs/latest/tuning.html

1. Tuning the data structures
   a) Avoid pointer based data structures and wrapper objects
   b) Array of object and primitive types
   c) Java fastutil
   d) Avoid nested structures
   e) Consider numeric IDs

2. Serialization
   When you cache RDDs, uses serialization eg: Kryo serialization.
   It may impact access time
   
3. Garbage collection tuning
   Add below parameters to spark Java options to find the GC details in worker nodes.  
   "-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"
   Cost of garbage collection is proportional to the number of Java objects.
   Goal of GC tuning in spark is to ensure that only long-lived objects are stored in Old generation and the Young 
   generation is sufficiently sized for short-lived objects. This will help avoid full GCs to collect temporary objects.
   
   a) Check if there are too many garbage collections by collecting GC stats. If full GC is invoked multiple times before
   the task is completed, there is not enough memory for execution.
   
   b) If there are many minor GC but not major GC, allocating more memory for Eden would help. if size of Eden is E, 
   young generation size would be (4/3) * E 
   
   c) If OldGen is close being full, reduce the memory used for caching, by lowering spark.memory.fraction.
   
   d) Try the G1GC garbage collector with -XX:+UseG1GC.
   
   e) As an example, if your task is reading data from HDFS, the amount of memory used by the task can be estimated 
   using the size of the data block read from HDFS. Note that the size of a decompressed block is often 2 or 3 times the 
   size of the block. So if we wish to have 3 or 4 tasks’ worth of working space, and the HDFS block size is 128 MB, we 
   can estimate size of Eden to be 4*3*128MB. 
   
4. Level of parallelism 
   Cluster will not be fully utilised unless you set the level of parallelism for each operation high enough. Spark
   automatically sets the number of map tasks to run on each file based on the file size. The distributed reduce 
   operations such as groupByKey, reduceByKey, it uses the largest parent RDD's number of partitions. You can pass the 
   level of parallelism as a second argument to these operations. 
   use spark.default.parallelism (for RDD)/ spark.sql.shuffle.partitions (for DF)

5. Memory usage of reduce tasks
   Spark shuffle operations build a HashTable with each task to perform grouping, which can often be large. Try to 
   increase the level of parallelism. Spark can efficiently support small tasks, as it re-uses JVM across tasks. You 
   can safely increase the level of parallelism more than the  number of cores. Recommended is 2-3 tasks per core.
   
6. Data locality 
   
7. Broadcasting large variables
   If the tasks are using large variable from driver program, consider broadcasting it.
   Tasks that are more than 20KB is worth optimizing.            
   