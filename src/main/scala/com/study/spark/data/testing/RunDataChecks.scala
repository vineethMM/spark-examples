package com.study.spark.data.testing

import org.apache.spark.sql.{DataFrame, SparkSession}

object RunDataChecks extends TestCases {
  val spark = SparkSession.builder()
    .appName("Sql with DataFrame")
    .config("spark.some.config.option", "some-value")
    .getOrCreate()

  import spark.implicits._

  def main(args: Array[String]): Unit = {
    val df = spark.read.parquet(args(0))

    val youngestPeople = getResultsAsString(df)

    println(getResultsAsString(df))
  }
}
