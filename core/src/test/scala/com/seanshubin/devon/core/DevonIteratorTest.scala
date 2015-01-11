package com.seanshubin.devon.core

import com.seanshubin.devon.core.devon.{Devon, DevonAssembler, DevonEnd, DevonRuleLookup}
import com.seanshubin.devon.core.token._
import org.scalatest.FunSuite

class DevonIteratorTest extends FunSuite {
  test("complex string") {
    val charIterator = SampleData.complexSample.toIterator
    val unfilteredTokenIterator: Iterator[Token] = new ParserIterator[Char, Token](
      charIterator, new TokenRuleLookup(), new TokenAssembler(), "token", TokenEnd)
    def validToken(token: Token): Boolean = {
      token match {
        case _: TokenWhitespaceBlock => false
        case _ => true
      }
    }
    val tokenIterator: Iterator[Token] = unfilteredTokenIterator.filter(validToken)
    val devonIterator: Iterator[Devon] = new ParserIterator[Token, Devon](
      tokenIterator, new DevonRuleLookup(), new DevonAssembler(), "element", DevonEnd)
    devonIterator.foreach(println)
  }
}
