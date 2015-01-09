package com.seanshubin.devon.core

import com.seanshubin.devon.core.token.{Token, TokenAssembler, TokenRuleLookup}
import org.scalatest.FunSuite

class TokenIteratorTest extends FunSuite {
  test("tokenize complex string") {
    val charIterator = SampleData.complexSample.toIterator
    val tokenIterator: Iterator[Token] = new ParserIterator[Char, Token](charIterator, new TokenRuleLookup(), new TokenAssembler(), "token")
    tokenIterator.foreach(println)
  }
}
