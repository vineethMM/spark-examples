## DataSet/DataFrame
Dataframe/Dataset were introduced in spark as part of structured data processing. What can a structured data processing can bring 
more than operations with RDD. In terms of end-user functionality, nothing much. But in terms of performance optimisation 
structured data processing can do many things. With RDD API the objects that constitute an RDDare completely opaque  to Spark framework
so that the framework has to relay on Java Object serialisation mechanisms to send an object between different executors or to cache
objects in memory with reduced memory foot print. By imposing structured data semantics, Spark can  now optimise memory usage,
garbage collection and even more like predicate push down. 

The new programming API in Spark unified Dataset and Dataframe APIs and now Dataframe is nothing but a type alias for Dataset[Row]
Hence all the operations on Dataframe is available with Dataset to. We will start with Dataframe, as it came to Spark first.

### Dataframes
Basic idea is to have API where you can switch between RDBS like SQL quries and more flexible RDDs in programming languages(Scala/Java/Python). This is achieved 
by making sure that when running a SQL query, results are returned as DataFrame. 

Most of the code here is from spark [programing guide](https://spark.apache.org/docs/latest/sql-programming-guide.html) with
minor modifications.

```
case class Person(name: String, age: Int)

val personDF: org.apache.spark.sql.DataFrame = List(Person("person1", 22), Person("person2", 40)).toDF // A simple way to create a Dataframe.

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

as you can see the columns in dataframe is referred by name. You no longer has type safety. Following code snippet demonstrate
how you can switch between SQL and scala style programming.
```
// Register the DataFrame as a global temporary view
personDF.createOrReplaceTempView("people")

val sqlDF = spark.sql("SELECT * FROM people")
sqlDF.show()

// +-------+----+
// |   name| age|
// +-------+----+
// |person1|  22|
// |person2|  40|
// +-------+----+

```

When dealing with DataFrame you will come across following two constructs frequently. Remember DataFrame = DataSet[Row]
> a) Row

> b) Column

we will look into these in little detail. From scala doc, definition of `Row` is as follows.

``Represents one row of output from a relational operator.  Allows both generic access by ordinal,
   which will incur boxing overhead for primitives, as well as native primitive access.``
   
i.e it is somewhat similar to one row in a JDBC ResultSet. 

```
// accessing values from a Row

val oneRow = personDF.head

// accessing by position
val nameOfPerson = oneRow.getString(0)
val ageOfperson  = oneRow.getInt(1)

// accesing by name
val nameOfPerson = oneRow.getAs[String]("name")
val ageOfPerson  = oneRow.getAs[Int]("age")

// programatically create a Row
val person3 = Row("name", "person3", "age", 1)
```

For Column, scala doc do not provide a definition, it says a Column can be computed from data of a Dataframe. 
In short, Column is an expression, which can be evaluated for each Row of a DataFrame. Following is from Scala doc.

```
/**
 * A column that will be computed based on the data in a DataFrame.
 * A new column is constructed based on the input columns present in a dataframe:
 *
 *   df("columnName")            // On a specific DataFrame.
 *   col("columnName")           // A generic column no yet associated with a DataFrame.
 *   col("columnName.field")     // Extracting a struct field
 *   col("`a.column.with.dots`") // Escape `.` in column names.
 *   $"columnName"               // Scala short hand for a named column.
 *   expr("a + 1")               // A column that is constructed from a parsed SQL Expression.
 *   lit("abc")                  // A column that produces a literal (constant) value.
 * 
 * Column objects can be composed to form complex expressions:
 *
 *   $"a" + 1
 *   $"a" === $"b"
```

You can add columns to or drop columns from an existing Dataframe. When you add columns to an existing column, you have make
sure that the newly added Column can be computed from existing columns in the dataframe. 

a Column from dataframe

```
 val ageColumn = personDF("age")
```

you can use this `ageColumn` in a select expression 
```
 personDF.select(ageColumn).show
 
 // +---+
 // |age|
 // +---+
 // | 22|
 // | 40|
 // +---+
```

Now, you can compute another column, say agePlusOne from an existing Column.
```
 val agePlusOne = ageColumn + 1
 
 personDF.select(agePlusOne).show
 
 // +---------+
 // |(age + 1)|
 // +---------+
 // |       23|
 // |       41|
 // +---------+
 
 // you can rename the column, using (ageColumn + 1).name("agePlusOne")
```

## Encoders and Decoders
By imposing structured data semantics, Spark can  now optimise memory usage,garbage collection and even more like 
predicate push down. All this is possible because spark know the structure of the data upfront. This enables spark to 
store the DataFrame as non-jvm objects and in an efficient way. Encoders and decoders are used to serialize and deserialize
these objects in memory.

By default Encoders and Decoders are defined only for basic objects in Scala, as a result you can only use primitive 
datatypes or case classed with primitive members in Dataset API.

If at all you need to write a custom complex class, you need to provide an encoder and decoder type classes too.

For more details about memory management and project tungsten, read the below blog post. 
https://databricks.com/blog/2015/04/28/project-tungsten-bringing-spark-closer-to-bare-metal.html