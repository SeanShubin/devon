package com.seanshubin.devon.core

import org.scalatest.FunSuite

class TokenIteratorTest extends FunSuite {
  test("tokenize complex string") {
    val tokenIterator:Iterator[Token] = TokenIterator.fromString(SampleData.complexSample)
    tokenIterator.foreach(println)
  }
}
