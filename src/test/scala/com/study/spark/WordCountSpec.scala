package com.study.spark

import com.study.spark.test.SparkSpec

class WordCountSpec extends SparkSpec {
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
