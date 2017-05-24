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

    val youngestPeople = findYoungestWithSql(peopleDF)

    println(
      s"""
         |Youngest people:
         |${youngestPeople.map(_.name).mkString("\n")}
       """.stripMargin
    )
  }

  def findYoungestWithDataset(peopleDS: Dataset[Person]): Set[Person] = {
    peopleDS
      .groupByKey(_.age)
      .mapGroups( (a, b) => (a, b))
      .reduce((a, b) => if(a._1 < b._1) a else b)
      ._2.toSet
  }
}
