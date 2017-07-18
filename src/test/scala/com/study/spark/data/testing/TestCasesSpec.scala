package com.study.spark.data.testing

import com.study.spark.test.SparkSpec
import org.apache.spark.sql.Row

class TestCasesSpec extends SparkSpec with TestCases {
  val spark = session
  import spark.implicits._

  case class Person(name: String, age: Int, card_num: Option[String], efft_d: String, expy_d: String)

  "TestCases" should "find primary key duplicates" in {
    val inputDF = List(
      Person("p1", 22, None, efft_d = "2016-11-12", expy_d = "2016-11-15"),
      Person("p1", 22, Some("1"), efft_d = "2016-11-16", expy_d = "2016-12-15"),
      Person("p1", 22, Some("2"), efft_d = "2016-12-16", expy_d = "2016-12-25"),
      Person("p2", 22, None, efft_d = "2016-11-12", expy_d = "2016-11-15"),
      Person("p2", 22, Some("1"), efft_d = "2016-11-16", expy_d = "2016-12-15"),
      Person("p2", 22, Some("2"), efft_d = "2016-12-16", expy_d = "2016-12-25")
    ).toDF

    primaryKeyDuplicates(inputDF, Array("name", "efft_d")).get should equal(Array[Row]())
  }

  "TestCases" should "find primary key duplicates" in {
    val rows = List(
      Person("p1", 22, None, efft_d = "2016-11-12", expy_d = "2016-11-15"),
      Person("p1", 22, Some("1"), efft_d = "2016-11-16", expy_d = "2016-12-15"),
      Person("p1", 22, Some("2"), efft_d = "2016-12-16", expy_d = "2016-12-25"),
      Person("p2", 22, None, efft_d = "2016-11-12", expy_d = "2016-11-15"),
      Person("p2", 22, Some("1"), efft_d = "2016-11-16", expy_d = "2016-12-15"),
      Person("p2", 22, Some("1"), efft_d = "2016-11-16", expy_d = "2016-12-15")
    )

    val inputDF = rows.toDF

    val expected = rows
      .groupBy(r => (r.name, r.efft_d))
      .collectFirst{ case ((name, efftD), rows) if rows.size > 1  => Row.fromTuple(("name", name, "efft_d", efftD))}
      .toArray

    primaryKeyDuplicates(inputDF, Array("name", "efft_d")) should equal(expected)
  }

}
