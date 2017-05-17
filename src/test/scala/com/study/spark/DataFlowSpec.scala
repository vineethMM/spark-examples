package com.study.spark

import com.study.spark.test.SparkSpec

class DataFlowSpec extends SparkSpec{
  "DataFlow" should "transform and dedup card data" in {
    val cardData = sc.parallelize(List(
      CardData("C1", "abcd", "2005-06-06", "2017-05-05"),
      CardData("C1", "abcd", "2005-06-06", "2017-05-06"),
      CardData("C1", "abcd", "2005-06-01", "2017-05-07"),
      CardData("C2", "defg", "2005-06-03", "2017-05-07"),
      CardData("C2", "defg", "2005-06-03", "2017-05-08"),
      CardData("C3", "efgh", "2005-06-03", "2017-05-08")
    ))

    val cardAccount = sc.parallelize(List(
      CardAccount("C1", "A1"),
      CardAccount("C2", "A2")
    ))

    val expectedOutput = Set(
      AccountData("A1", "abcd", "2005-06-06", "2017-05-05"),
      AccountData("A2", "defg", "2005-06-03", "2017-05-07")
    )

    val actualOutput = DataFlow.tranformAndDedup(cardData, cardAccount).collect.toSet

    actualOutput should equal(expectedOutput)
  }
}
