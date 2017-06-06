package com.study.spark.data.testing

import org.apache.spark.sql.{DataFrame, Row}

import org.apache.spark.sql.functions._

trait TestCases {
  def primaryKeyDuplicates(peopleDF: DataFrame, primaryKeys: List[String]): Array[Row]= {
    val countPredicate = col("count") > 1

    peopleDF.select(primaryKeys(0), primaryKeys.tail:_*)
      .groupBy(primaryKeys(0), primaryKeys.tail:_*)
      .count
      .filter(countPredicate)
      .limit(10)
      .collect
  }

  def nullCounts(peopleDF: DataFrame): List[(String, Long)] = {
    val nullCountAggregators =
      ("row count", column("*")) :: peopleDF
        .columns
        .map(c => (c, count(when(col(c).isNull, 1))))
        .toList

    val aggregatedRow = peopleDF
      .agg(nullCountAggregators.head._2, nullCountAggregators.map(_._2):_*)
      .collect
      .head

    nullCountAggregators
      .map{ case (name, coulmn) => (name, aggregatedRow.getAs[Long](coulmn.toString))}
  }
}