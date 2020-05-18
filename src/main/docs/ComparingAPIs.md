
Compare 3 APIs in spark.
========================

To name the APIs, it is RDD, DataFrame and DataSet APIs.

Even though there seems to be 3 APIs, if you have a closed look, we can see that its actually 2.

The DataFrame API is nothing but a type alias for DataSet[Row]

1. RDD is the basic and low level API. It is the building block abstraction of Spark. Resilient Distributed Dataset.
It handles the basic functions such as immutability, distribution, fault tolerance, data locality etc. Good thing about
this API is that it allows arbitrary datatypes (good fit for unstructured data). It uses java serialization to serialize 
JVM objects and to send across the network.

2. DataFrame API: This is build on the SQL engine. As I mentioned earlier, it is a type alias for DataSet[Raw], where Raw 
is generic type that can hold columns of rows, and retrieve the values using column names/position. i.e it requires a schema. 
It is a good fit when it comes to structured data. Defining a schema upfront gives Spark an opportunity to better manage 
the Memory utilisation. It helps Spark to use advanced memory utilisation techniques (including off-heap memory). Project 
Tungsten is built for this. It can avoid the memory overheads with Java Object serialisation. 
Another advantage using this API is process optimisation.
a) Logical optimisation
   Rule based optimisations
   Predicate push down
   projection pruning 
b) Physical optimisation
   cost based optimisation
   Multiple physical plans, especially used for Joins 
   
3. DataSet API.
   All the advantages of DataFrame API
   + Type safety
   Case classes with basic types are supported out of the box.
   If you need custom objects, we need to define encoders and decoders. This is an experimental feature. This encoder is 
   nothing but the interfaces that gives spark the ability to serialize and deserialize the objects with Java serialisation.
   
   ```
   trait Encoder[T] extends Serializable {
   
     /** Returns the schema of encoding this type of object as a Row. */
     def schema: StructType
   
     /**
      * A ClassTag that can be used to construct and Array to contain a collection of `T`.
      */
     def clsTag: ClassTag[T]
   }
   ```
   
   
   ```case class StructType(fields: Array[StructField])```
   