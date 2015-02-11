package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.ParserIterator
import com.seanshubin.devon.core.token.{Token, TokenMarshallerImpl, TokenWhitespace}

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

class DevonMarshallerImpl extends DevonMarshaller {
  private val ruleLookup = new DevonRuleLookup
  private val assembler = new DevonAssembler
  private val tokenMarshaller = new TokenMarshallerImpl
  private val compactFormatter = new CompactDevonFormatter
  private val prettyFormatter = new PrettyDevonFormatter

  override def stringToIterator(text: String): Iterator[Devon] = {
    val charIterator = text.toIterator
    charsToIterator(charIterator)
  }

  override def fromString(text: String): Devon = {
    stringToIterator(text).next()
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
      tokenIterator, ruleLookup, assembler, "element", DevonEnd)
    devonIterator
  }

  override def charsToIterator(charIterator: Iterator[Char]): Iterator[Devon] = {
    val tokenIterator = tokenMarshaller.charsToIterator(charIterator)
    tokensToIterator(tokenIterator)
  }

  override def toCompact(devon: Devon): String = compactFormatter.format(devon)

  override def toCompact(devonIterator: Iterator[Devon]): String = compactFormatter.format(devonIterator)

  override def toPretty(devon: Devon): Seq[String] = prettyFormatter.format(devon)

  override def fromValue[T: ru.TypeTag : ClassTag](value: T): Devon = ???

  override def toValue[T](devon: Devon, theClass: Class[T]): T = ???
}
