package com.seanshubin.devon.core.token

import com.seanshubin.devon.core.ParserIterator

class TokenMarshallerImpl extends TokenMarshaller {
  private val ruleLookup = new TokenRuleLookup()
  private val assembler = new TokenAssembler()

  override def charsToIterator(charIterator: Iterator[Char]): Iterator[Token] = {
    val tokenIterator = new ParserIterator[Char, Token](
      charIterator, ruleLookup, assembler, "token", TokenEnd)
    tokenIterator
  }

  override def stringToIterator(text: String): Iterator[Token] = {
    val charIterator = text.toIterator
    charsToIterator(charIterator)
  }
}
