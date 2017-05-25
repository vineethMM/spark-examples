package com.study.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

case class CardData(cardNumber: String, cardHolder: String,lastBillDate: String, extractDate: String)

case class CardAccount(cardNumber: String, accountNumber: String)

case class AccountData(accountNumber: String, cardHolder: String,lastBillDate: String, extractDate: String)

object DataFlow {

  def transformAndDedup(cardData: RDD[CardData], cardAccount: RDD[CardAccount]): RDD[AccountData] = {
    cardData
      .map(r => (r.cardNumber, r))
      .reduceByKey{ case (l, r) => if(l.extractDate < r.extractDate) l else r }
      .join(cardAccount.map(r => (r.cardNumber, r)))
      .values
      .map{ case (card, acct) => AccountData(acct.accountNumber, card.cardHolder, card.lastBillDate, card.extractDate) }
  }

  def readCardData(sc: SparkContext, inputPath: String): RDD[CardData] = {
    sc.textFile(inputPath)
      .map(_.split(","))
      .collect{ case Array(cardNumber, cardHolder, lastBilldate, extractdate) =>
        CardData(cardNumber, cardHolder, lastBilldate, extractdate)
      }
  }

  def readCardAccount(sc: SparkContext, inputPath: String): RDD[CardAccount] = {
    sc.textFile(inputPath)
      .map(_.split(","))
      .collect{ case Array(cardNumber, accoutNumber) => CardAccount(cardNumber, accoutNumber)}
  }

  def main(args: Array[String]): Unit = {
    assert(args.length == 2,
      s"""
         |Expects exactly 3 arguments
         |<card_data_path> <accout_data_path> <output_path>
       """.stripMargin)

    val conf = new SparkConf().setAppName("LinearRegressionWithSGDExample")
    val sc = new SparkContext(conf)

    val cardData = readCardData(sc, args(0))
    val accountData = readCardAccount(sc, args(1))

    transformAndDedup(cardData, accountData).saveAsTextFile(args(2))
  }
}
