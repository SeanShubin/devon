package com.seanshubin.devon.domain

import com.seanshubin.devon.tokenizer.Token

import scala.reflect.runtime.universe

trait DevonMarshaller {
  def charsToIterator(charIterator: Iterator[Char]): Iterator[Devon]

  def tokensToIterator(tokenIterator: Iterator[Token]): Iterator[Devon]

  def toCompact(devon: Devon): String

  def toCompact(devonIterator: Iterator[Devon]): String

  def toPretty(devon: Devon): Seq[String]

  def fromValue[T: universe.TypeTag](value: T): Devon

  def toValue[T: universe.TypeTag](devon: Devon, theClass: Class[T]): T

  final def stringToIterator(text: String): Iterator[Devon] =
    charsToIterator(text.iterator)

  final def fromString(text: String): Devon =
    stringToIterator(text).next()

  final def stringToValue[T: universe.TypeTag](text: String, theClass: Class[T]): T = {
    val devon = fromString(text)
    val value = toValue(devon, theClass)
    value
  }

  final def valueToCompact[T: universe.TypeTag](value: T): String = {
    val devon = fromValue(value)
    val compact = toCompact(devon)
    compact
  }

  final def valueToPretty[T: universe.TypeTag](value: T): Seq[String] = {
    val devon = fromValue(value)
    val pretty = toPretty(devon)
    pretty
  }
}
