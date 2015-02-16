package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.token.Token

import scala.reflect.runtime.universe

trait DevonMarshaller {
  def stringToIterator(text: String): Iterator[Devon]

  def charsToIterator(charIterator: Iterator[Char]): Iterator[Devon]

  def tokensToIterator(tokenIterator: Iterator[Token]): Iterator[Devon]

  def fromString(text: String): Devon

  def toCompact(devon: Devon): String

  def toCompact(devonIterator: Iterator[Devon]): String

  def toPretty(devon: Devon): Seq[String]

  def fromValue[T: universe.TypeTag](value: T): Devon

  def toValue[T: universe.TypeTag](devon: Devon, theClass: Class[T]): T
}
