package com.seanshubin.devon.core.token

import com.seanshubin.devon.core.ParserIterator

object TokenIterator {
  private val ruleLookup = new TokenRuleLookup()
  private val assembler = new TokenAssembler()

  def fromString(value: String): Iterator[Token] = {
    val charIterator = value.toIterator
    fromCharIterator(charIterator)
  }

  def fromCharIterator(charIterator: Iterator[Char]): Iterator[Token] = {
    val tokenIterator = new ParserIterator[Char, Token](
      charIterator, ruleLookup, assembler, "token", TokenEnd)
    tokenIterator
  }
}
