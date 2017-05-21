package com.study.spark.df

import org.apache.spark.sql.{DataFrame, SparkSession}

case class Person(name: String, gender: String, age: Int)

object DateFrameSQL {
  val spark = SparkSession
    .builder()
    .appName("Sql with DataFrame")
    .config("spark.some.config.option", "some-value")
    .getOrCreate()

  import spark.implicits._

  def main(args: Array[String]): Unit = {
    val peopleDF = spark.read.json("examples/src/main/resources/json/people.json")

    val youngestPeople = findYoungestWithSql(peopleDF)

    println(
      s"""
         |Youngest poeple:
         |${youngestPeople.map(_.name).mkString("\n")}
       """.stripMargin
    )
  }

  def findYoungestWithSql(peopleDF: DataFrame): Set[Person] = {
    peopleDF
      .join(peopleDF.groupBy().min("age"), $"age" === $"min(age)")
      .select('name, 'gender ,'age)
      .collect
      .map(r => Person(r.getAs[String]("name"), r.getAs[String]("gender"), r.getAs[Int]("age")))
      .toSet
  }
}
