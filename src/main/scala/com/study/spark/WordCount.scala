package com.study.spark

import org.apache.spark._, SparkContext._
import org.apache.spark.rdd.RDD

object WordCount {
  val appName = "spark-word-count"

  def countWordsAndPersist(sc: SparkContext, inputPath: String, outputPath: String) = {
    val input = sc.textFile(inputPath)
    val countByWord = countWords(input)

    countByWord.saveAsTextFile(outputPath)
  }

  def countWords(inputRdd: RDD[String]): RDD[(String, Int)] =
    inputRdd
      .flatMap(_.split("\\s+")) // split words
      .map(w => (w, 1))
      .reduceByKey(_ + _)

  def main(args: Array[String]): Unit = {
   assert(
     args.length == 3,
     s"""
        |Application expects exactly 3 arguments provided ${args.length}
        |
        |<master> <input_path> <output_path>
      """.stripMargin
   )

    val conf = new SparkConf()
      .setAppName(appName)
      .setMaster(args(0))

    // A SparkContext represents the connection to a Spark cluster
    val sc = new SparkContext(conf)

    // all the action goes here
    countWordsAndPersist(sc, args(1), args(2))
  }
}