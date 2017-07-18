package com.study.spark.data.testing

trait Result[A] {
  def get: A

  /**
    * @return a formatted string that will appear on the output.
    */
  def present: String
}

case class SimpleResult[A](get: A, present: String) extends Result[A]

object Result {
  def apply[A](value: A, message: String): Result[A] = SimpleResult(value, message)

  def apply[A](value:A, fn: A => String): Result[A] =
    SimpleResult(value, fn(value))
}
