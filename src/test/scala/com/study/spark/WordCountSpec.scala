package com.study.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{FlatSpec, Matchers}

class WordCountSpec extends FlatSpec with Matchers{
  val conf = new SparkConf()
      .setMaster("local[2]")
        .setAppName("word-count")
  val sc = new SparkContext(conf)


  "WordCount" should  "count words" in {
    val inputRdd = sc.parallelize(List(
      "To be is to do",
      "To do is to be"
    ))

   val wordCounts = WordCount.countWords(inputRdd).collect.toSet


    wordCounts should equal(Set(
      ("To", 2),
      ("to", 2),
      ("is", 2),
      ("be", 2),
      ("is", 2),
      ("do", 2)
    ))
  }

}
