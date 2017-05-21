package com.study.spark.df

import com.study.spark.test.SparkSpec

class DataFrameSQLSpec extends SparkSpec {

  "DataFrameSQL" should "find the youngest person" in {

    val spark = session
    import spark.implicits._


    val inputDF = List(
      Person("p1", "M", 22),
      Person("p2", "F", 45),
      Person("p3", "M", 22),
      Person("p4", "F", 26)
    ).toDF

    DateFrameSQL.findYoungestWithSql(inputDF) should equal(Set(
      Person("p1", "M", 22),
      Person("p3", "M", 22)
    ))
  }
}
