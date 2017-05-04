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
    val conf = new SparkConf()
      .setAppName(appName)

    // A SparkContext represents the connection to a Spark cluster
    val sc = new SparkContext(conf)

    // all the action goes here
    countWordsAndPersist(sc, args(0), args(1))
  }
}