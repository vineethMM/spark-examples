package com.study.spark

import org.apache.spark._, SparkContext._
import org.apache.spark.rdd.RDD

object WordCount {

  def countWordsAndPersist(sc: SparkContext, inputPath: String, outputPath: String) = {
    val input = sc.textFile(inputPath)
    val countByWord = countWords(input)

    countByWord.saveAsTextFile(outputPath)
  }

  def countWords(inputRdd: RDD[String]): RDD[(String, Int)] =
    inputRdd
      .flatMap(_.split("\\s+").map(w => (w, 1)))
      .reduceByKey(_ + _)
}