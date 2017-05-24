package com.study.spark.ds

import com.study.spark.test.SparkSpec

import com.study.spark.df.Person

class DataSetSQLSpec  extends SparkSpec{
  "DataSetSQl" should "find the youngest person" in {
    val spark = session
    import spark.implicits._


    val inputDS = List(
      Person("p1", "M", 22),
      Person("p2", "F", 45),
      Person("p3", "M", 22),
      Person("p4", "F", 26)
    ).toDS

    DataSetSQL.findYoungestWithDataset(inputDS) should equal(Set(
      Person("p1", "M", 22),
      Person("p3", "M", 22)
    ))
  }
}
