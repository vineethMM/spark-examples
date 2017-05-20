## WordCount program explained.

Please refer to `com.study.spark.WordCount` in src folder.

Its important to note that WordCount program do not leverage any of the differentiating capabilities of Spark. It is just the Hello World
program to get started with Spark.

We start with creating a SparkConfig object in the `main` method. SparkConfig is basically a set of key-value pairs that holds configuration items. In our program, we have set `AppName` explicitly and let all others fallback to their defaults.

Second step is to create SparkContext. SparkContext is the main entry point of a Spark program. We can use spark context to create RDDs,
broadcast variables and accumulators. These RDDs, broad cast variables and accumulators are only available with the Spark Context.
[More on SparkContext here](https://jaceklaskowski.gitbooks.io/mastering-apache-spark/content/spark-sparkcontext.html)

Once you have initialised SparkContext next step is to create a RDD[String] from the input text file.
Its very easy, `sc.textFile(inputPath)` will return a RDD[String]. Each RDD has a set of partitions, by default the RDD we just
created will have as many number of partitions as the number of HDFS blocks our HDFS files have. If you want to change the 
number of partitions you can specify it in function call like `sc.textFile(inputpath, 10)`. 

Ok, now we have our RDD[String] ready. Next is to split the lines into words.

val records: RDD[String] = sc.textFile("/path/to/file")

val words:RDD[String] = records.flatMap(_.split("\\s+"))

`flatMap` is a transformation available on RDD. We have two types of methods available on RDD transformations and actions.
transformations on a RDD always return another RDD, where as actions on an RDD return a value to the driver program. For the
list of transformations and actions available on RDDs please visit this [page.](https://spark.apache.org/docs/latest/programming-guide.html#transformations)

Next step in our program is to map each word to a key-value pair where `key` is the word itself and value is number of occurance
which will be always 1. 

val wordsAndOne:RDD[(String, Int)] = words.map(w => (w, 1)

what we get back is a paired RDD[(String, Int)], with paired RDD we have more transformations and actions available
than a plain RDD. In our word count program we would like to sum the values by key(or by word). To accomplish that
we use the following transformation

val wordsAndCount:RDD[(String, Int)]  = wordsAndOne.reduceByKey(_ + _)

Again you get back a RDD[(String, Int)], but this time you have onle one occurance of each word in our RDD along with 
it's count.

Now, persist result RDD back in HDFS. 

wordsAndCount.saveAsTextFile("path/in/hdfs")

