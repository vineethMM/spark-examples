package com.study.spark.test

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers, Suite}

trait UnitSpec extends FlatSpec with Matchers

trait SparkSupport extends BeforeAndAfterAll { self: Suite =>
  private var _sc: SparkContext = null
  private var _session: SparkSession = null

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    val conf = new SparkConf(false)
        .setMaster("local[2]")
        .setAppName("spark-unit-test")

    _sc = new SparkContext(conf)
    _session = SparkSession
      .builder()
      .appName("Sql with DataFrame")
      .master("local[2]")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
  }

  override protected def afterAll(): Unit = {
    try{
      if(_sc != null)
        _sc.stop

      if(_session != null)
        _session.stop

      _sc =  null
      _session = null
    } finally {
      super.afterAll
    }
  }

  def sc = _sc
  def session = _session
}

abstract class SparkSpec extends UnitSpec with SparkSupport