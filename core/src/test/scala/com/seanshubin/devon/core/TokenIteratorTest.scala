package com.seanshubin.devon.core

import com.seanshubin.devon.core.token.{TokenIterator, Token}
import org.scalatest.FunSuite

class TokenIteratorTest extends FunSuite {
  test("tokenize complex string") {
    val tokenIterator: Iterator[Token] = TokenIterator.fromString(SampleData.complexSample)
    tokenIterator.foreach(println)
  }
}
