package com.study.spark.data.testing

import org.apache.spark.sql.{Column, DataFrame, Row}
import org.apache.spark.sql.functions._

import scala.reflect.ClassTag

trait TestCases {
  type Date = String

  def primaryKeyDuplicates(peopleDF: DataFrame, primaryKeys: Array[String]): Result[Array[Row]] = {
    val countPredicate = col("count") > 1

    val keysWithDuplicates = peopleDF
      .select(primaryKeys(0), primaryKeys.tail:_*)
      .groupBy(primaryKeys(0), primaryKeys.tail:_*)
      .count
      .filter(countPredicate)
      .limit(10)
      .collect


    Result[Array[Row]](
      keysWithDuplicates,
      rows => {
        if(rows.nonEmpty){
          s"""Primary key ${primaryKeys.mkString(",")} has got duplicates. Following are some examples:
             |${rows.mkString("",",","\n")}
           """.stripMargin
        } else {
          s"Primary key ${primaryKeys.mkString(",")} has no duplicates"
        }
      }
    )
  }

  def aggrChecks(peopleDF: DataFrame): Result[(List[(String, Long)], List[(String, Long)])] = {
    val nullCountAggregators =
      ("row count", count(column("*"))) :: peopleDF
        .columns
        .map(c => (c, count(when(col(c).isNull, 1))))
        .toList

    val appxDistCountAggs =
      peopleDF
        .columns
        .map(r => (s"approx_count_distinct($r)", approx_count_distinct(r)))
        .toList

    val aggregatedRow = peopleDF
      .agg(nullCountAggregators.head._2, (nullCountAggregators ++ appxDistCountAggs).tail.map(_._2):_*)
      .collect
      .head

    val nullCounts = nullCountAggregators.map{case (name, _) => (name, aggregatedRow.getAs[Long](name))}
    val approxCounts = appxDistCountAggs.map{case (name, _) => (name, aggregatedRow.getAs[Long](name))}

    Result[(List[(String, Long)], List[(String, Long)])](
      (nullCounts, approxCounts),
      _ match {
        case (nullCounts, approxCounts) =>
          s"""
             |Null count of columns is as follows
             |===================================
             |${nullCounts.map{ case (column, count) => s"${column} => ${count}" }.mkString("  ", "", "\n")}
             |
             |Approximate distinct count of columns are as follows.
             |=====================================================
             |${approxCounts.map{ case (column, count) => s"${column} => ${count}" }.mkString("  ", "", "\n")}
             |${appxDistCountAggs}
           """.stripMargin
      }
    )
  }

//  def checkForTimeVariance[K : ClassTag: Ordering](
//    df: DataFrame,
//    key: Row => K,
//    efftD: Row => LocalDate = _.getAs[String]("efft_d").toLocalDate,
//    expyD: Row => LocalDate = _.getAs[String]("expy_d").toLocalDate
//  ): String = {
//    val errors = df
//      .rdd
//      .map(r => (key(r), efftD(r),expyD(r)))
//      .groupBy{ case (key, _, _) => key }
//      .mapValues{ vs =>
//        vs
//          .toList
//          .sortBy{case (_, efftD, _) => efftD}
//          .sliding(2)
//          .exists{
//            case l :: r :: Nil => l._3.plusDays(1) != r._2
//            case l :: Nil => true
//            case _ => false
//          }
//      }
//      .filter{ case (_, violatesTimeVariance) => violatesTimeVariance }
//      .take(10)
//      .map(_._1)
//
//    if(errors.nonEmpty) s"Errors in time variance"
//    else "Time variance holds true"
//  }
}