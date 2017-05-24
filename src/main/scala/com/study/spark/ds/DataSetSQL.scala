package com.study.spark.ds

import org.apache.spark.sql.{Dataset, SparkSession}

import com.study.spark.df.Person

object DataSetSQL {
  val spark = SparkSession
    .builder()
    .appName("Sql with DataFrame")
    .config("spark.some.config.option", "some-value")
    .getOrCreate()

  import spark.implicits._

  def main(args: Array[String]): Unit = {
    val peopleDS = spark
      .read
      .json("examples/src/main/resources/json/people.json")
      .as[Person]

    val youngestPeople = findYoungestWithDataset(peopleDS)

    println(
      s"""
         |Youngest people:
         |${youngestPeople.map(_.name).mkString("\n")}
       """.stripMargin
    )
  }

  def findYoungestWithDataset(peopleDS: Dataset[Person]): Set[Person] = {
    val minAge = peopleDS.map(_.age).reduce(_ min _)

    peopleDS
      .filter(_.age == minAge)
      .collect
      .toSet
  }
}
