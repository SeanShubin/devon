package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.ParserIterator
import com.seanshubin.devon.core.token._

object DevonIterator {
  private val ruleLookup = new DevonRuleLookup()
  private val assembler = new DevonAssembler()

  def fromString(value: String): Iterator[Devon] = {
    val charIterator = value.toIterator
    fromCharIterator(charIterator)
  }

  def fromCharIterator(charIterator: Iterator[Char]): Iterator[Devon] = {
    val tokenIterator = TokenIterator.fromCharIterator(charIterator)
    fromTokenIterator(tokenIterator)
  }

  def fromTokenIterator(unfilteredTokenIterator: Iterator[Token]): Iterator[Devon] = {
    def notWhitespace(token: Token): Boolean = {
      token match {
        case _: TokenWhitespace => false
        case _ => true
      }
    }
    val tokenIterator = unfilteredTokenIterator.filter(notWhitespace)
    val devonIterator = new ParserIterator[Token, Devon](
      tokenIterator, ruleLookup, assembler, "element", DevonEnd)
    devonIterator
  }

  def parse(value: String): Devon = {
    fromString(value).next()
  }
}
