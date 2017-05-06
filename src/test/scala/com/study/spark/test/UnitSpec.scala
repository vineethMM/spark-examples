package com.study.spark.test

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers, Suite}

trait UnitSpec extends FlatSpec with Matchers

trait SparkSupport extends BeforeAndAfterAll { self: Suite =>
  private var _sc: SparkContext = null

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    val conf = new SparkConf()
        .setMaster("local[2]")
        .setAppName("spark-unit-test")

    _sc = new SparkContext(conf)
  }

  override protected def afterAll(): Unit = {
    try{
      if(_sc != null)
        _sc.stop

      _sc =  null
    } finally {
      super.afterAll
    }
  }

  def sc = _sc
}

abstract class SparkSpec extends UnitSpec with SparkSupport