## Why RDD ?

In Spark, the programming paradigm remained the same: Map-Reduce. Spark, at its core is an
implementation of Map-Reduce paradigm. The nature of problems it can solve also is almost
same as that of Hadoop Map-reduce. What makes it different is the speed at which it can execute 
and the caching mechanism which avoids disk IOs whenever possible. 

Now, the question is why RDD (Resilient Distributed Data-set) abstraction ?
In Hadoop Map-Reduce, one job communicate to the other by writing data into HDFS and reading from there.
Even if we are not interested in persisting the intermediate results in a data flow having multiple map-reduce jobs,
we have to do it (or the the framework will do it) because that is the only way jobs can communicates with each other.
But it has got advantages. HDFS will take care of fault tolerance and high availability of your data-sets.
As I said earlier, Spark tries to avoid Disk IOs whenever possible( It is important to not the `whenever` because the spark
caching mechanism is not only in memory, but it can vary depending on the storage level you choose and may include DISK IOs).
In order to avoid disk IOs, we need spark to cache the intermediate results in memory. This demands a whole new 
mechanisms for fault tolerance. You should be able to guess, why. In Hadoop map-reduce, the file system,
HDFS handles it. Once the data in written, eveything is taken care by HDFS. The processing framework has nothing to worry
about.
 
In HDFS, the file blocks are replicated and stored in different machines for fault tolerance. One more thing to note here is that
you have serialisation overhead in all these operations too. Think of the same strategy with memory. It is costly to replicate your data in memory, both in terms of storage and network transfer. We need a better solution. (Note: even if it is costly, we would like to do it at times and spark has provision for that as well).
 
The better soution Spark came up with is RDD. RDD is a read-only, partitioned collection of records. RDDs can only be created 
through functional operations (deterministic operations) on other RDDs or from files systems like HDFS which
are reliable persistent storage.The abstraction of RDD holds meta information including the previous RDD, transformation applied to the
previous RDD, the list of partitions and their loications etc. This make RDD capable of reconstructing any partition
in case of a failure, most of the time by just re-computing the lost partition. This property makes it efficient for batch processing.

I would strongly encourage you to read the RDD white paper, its [here](https://www.usenix.org/system/files/conference/nsdi12/nsdi12-final138.pdf)

### RDD Representation

In a nutshell each RDD carries following information.

* Set of partitions: Atomic pieces of information.
* Set of dependencies on parent RDDs
* A function: For computing the data-set based on its parent.
* Meta data about partitioning scheme.
* Data placement (Important for data locality).




