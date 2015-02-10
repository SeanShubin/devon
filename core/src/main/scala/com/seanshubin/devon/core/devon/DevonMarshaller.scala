package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.token.Token

import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}

trait DevonMarshaller {
  def stringToIterator(text: String): Iterator[Devon]

  def charsToIterator(charIterator: Iterator[Char]): Iterator[Devon]

  def tokensToIterator(tokenIterator: Iterator[Token]): Iterator[Devon]

  def stringToAbstractSyntaxTree(text: String): Devon

  def toCompact(devon: Devon): String

  def toPretty(devon: Devon): Seq[String]

  def valueToAbstractSyntaxTree[T: ru.TypeTag : ClassTag](value: T): Devon

  def toClass[T](devon: Devon, theClass: Class[T]): T
}
