
Dataframe/Dataset were introduced in spark as part of structured data processing. What can a structured data processing can bring 
more than operations with RDD. In terms of end-user functionality, nothing much. But in terms of performance optimisation 
structured data processing can do many things. With RDD API the objects that constitute an RDDare completely opaque  to Spark framework
so that the framework has to relay on Java Object serialisation mechanisms to send an object between different executors or to cache
objects in memory with reduced memory foot print. By imposing structured data semantics, Spark can  now optimise memory usage,
garbage collection and even more like predicate push down. 

The new programming API in Spark unified Dataset and Dataframe APIs and now Dataframe is nothing but a type alias for Dataset[Row]
Hence all the operations on Dataframe is available with Dataset to. We will start with Dataframe, as it came to Spark first.

### Dataframes
 Basic idea is to have API where you can 
switch between RDBS like SQL quries and more flexible RDDs in programming languages(Scala/Java/Python). This is achieved 
by making sure that when running a SQL query, results are returned as DataFrame. 

```
case class Person(name: String, age: Int)

val personDF: DataFrame = List(Person("person1", 22), Person("person2", 40)).toDF // A simple way to create a Dataframe.

// Its not a typical use of dataframe, If you have schema upfront ready in a case class spark prefer to use 
// DataSet[Person] rather tha Dataframe. 
// Here it is for demonstration purpose only.

personDF.show

// +-------+----+
// |   name| age|
// +-------+----+
// |person1|  22|
// |person2|  40|
// +-------+----+

personDF.select('name).show

+-------+
|   name|
+-------+
|person1|
|person2|
+-------+
```

as you can see the columns in 