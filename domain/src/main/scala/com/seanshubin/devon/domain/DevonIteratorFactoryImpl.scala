package com.seanshubin.devon.domain

import com.seanshubin.devon.parser.{Assembler, ParserIterator, RuleLookup}
import com.seanshubin.devon.tokenizer.{Token, TokenMarshaller, TokenWhitespace}

class DevonIteratorFactoryImpl(ruleLookup: RuleLookup[Token],
                               assembler: Assembler[Token, Devon],
                               tokenMarshaller: TokenMarshaller) extends DevonIteratorFactory {
  override def stringToIterator(text: String): Iterator[Devon] = {
    charsToIterator(text.toIterator)
  }

  override def charsToIterator(charIterator: Iterator[Char]): Iterator[Devon] = {
    tokensToIterator(tokenMarshaller.charsToIterator(charIterator))
  }

  override def tokensToIterator(unfilteredTokenIterator: Iterator[Token]): Iterator[Devon] = {
    def notWhitespace(token: Token): Boolean = {
      token match {
        case _: TokenWhitespace => false
        case _ => true
      }
    }
    val tokenIterator = unfilteredTokenIterator.filter(notWhitespace)
    val devonIterator = new ParserIterator[Token, Devon](
      tokenIterator, ruleLookup, assembler, "element")
    devonIterator
  }
}
