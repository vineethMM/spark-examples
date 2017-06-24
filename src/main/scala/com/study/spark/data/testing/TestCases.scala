package com.study.spark.data.testing

import com.github.nscala_time.time.Imports._
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{Column, DataFrame, Row}
import org.apache.spark.sql.functions._

import scala.reflect.ClassTag

trait TestCases {
  type Date = String

  def primaryKeyDuplicates(peopleDF: DataFrame, primaryKeys: List[String]): Array[Row]= {
    val countPredicate = col("count") > 1

    peopleDF.select(primaryKeys(0), primaryKeys.tail:_*)
      .groupBy(primaryKeys(0), primaryKeys.tail:_*)
      .count
      .filter(countPredicate)
      .limit(10)
      .collect
  }

  def aggrChecks(peopleDF: DataFrame) = {
    val nullCountAggregators =
      ("row count", count(column("*"))) :: peopleDF
        .columns
        .map(c => (c, count(when(col(c).isNull, 1))))
        .toList

    val appxDistCountAggs =
      peopleDF
        .columns
        .map(r => (s"approx_count_distinct($r)", approx_count_distinct(r)))

    val aggregatedRow = peopleDF
      .agg(nullCountAggregators.head._2, (nullCountAggregators ++ appxDistCountAggs).tail.map(_._2):_*)
      .collect
      .head

    (nullCountAggregators ++ appxDistCountAggs)
      .map{ case (name, coulmn) => (name, aggregatedRow.getAs[String](coulmn.toString))}toString
  }

  def checkForTimeVariance[K : ClassTag: Ordering](
    df: DataFrame,
    key: Row => K,
    efftD: Row => LocalDate = _.getAs[String]("efft_d").toLocalDate,
    expyD: Row => LocalDate = _.getAs[String]("expy_d").toLocalDate
  ): Array[K] = {
    df
      .rdd
      .map(r => (key(r), efftD(r),expyD(r)))
      .groupBy{ case (key, _, _) => key }
      .mapValues{ vs =>
        vs
          .toList
          .sortBy{case (_, efftD, _) => efftD}
          .sliding(2)
          .exists{
            case l :: r :: Nil => l._3.plusDays(1) != r._2
            case l :: Nil => true
            case _ => false
          }
      }
      .filter{ case (_, violatesTimeVariance) => violatesTimeVariance }
      .take(10)
      .map(_._1)
  }
}