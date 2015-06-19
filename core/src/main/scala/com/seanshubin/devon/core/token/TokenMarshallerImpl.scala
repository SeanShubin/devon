package com.seanshubin.devon.core.token

import com.seanshubin.devon.parser.{ParserIterator, StringProcessor}

class TokenMarshallerImpl(stringProcessor: StringProcessor) extends TokenMarshaller {
  private val ruleLookup = new TokenRuleLookup()
  private val assembler = new TokenAssembler(stringProcessor)

  override def charsToIterator(charIterator: Iterator[Char]): Iterator[Token] = {
    val tokenIterator = new ParserIterator[Char, Token](
      charIterator, ruleLookup, assembler, "token")
    tokenIterator
  }

  override def stringToIterator(text: String): Iterator[Token] = {
    val charIterator = text.toIterator
    charsToIterator(charIterator)
  }
}
