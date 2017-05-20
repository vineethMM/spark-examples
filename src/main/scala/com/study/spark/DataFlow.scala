package com.study.spark

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

case class CardData(cardNumber: String, cardHolder: String,lastBillDate: String, extractDate: String)

case class CardAccount(cardNumber: String, accountNumber: String)

case class AccountData(accountNumber: String, cardHolder: String,lastBillDate: String, extractDate: String)

object DataFlow {

  def tranformAndDedup(cardData: RDD[CardData], cardAccount: RDD[CardAccount]): RDD[AccountData] = {
    //TODO Fix me
  }

  def readCardData(sc: SparkContext, inputPath: String): RDD[CardData] = {
    // TODO Fix me
  }

  def readCardAccount(sc: SparkContext, inputPath: String): RDD[CardAccount] = {
    // TODO Fix me
  }

  def main(args: Array[String]): Unit = {
    // TODO Read arguments
    // constrcuts RDDs
    // transform and dedup
    // persist data
  }
}
