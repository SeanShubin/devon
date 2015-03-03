package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.token.Token

import scala.reflect.runtime.universe

class DevonMarshallerImpl(compactFormatter: CompactDevonFormatter,
                          prettyFormatter: PrettyDevonFormatter,
                          devonReflection: DevonReflection,
                          iteratorFactory: DevonIteratorFactory) extends DevonMarshaller {
  override def charsToIterator(charIterator: Iterator[Char]): Iterator[Devon] =
    iteratorFactory.charsToIterator(charIterator)

  override def tokensToIterator(unfilteredTokenIterator: Iterator[Token]): Iterator[Devon] =
    iteratorFactory.tokensToIterator(unfilteredTokenIterator)

  override def toCompact(devon: Devon): String =
    compactFormatter.format(devon)

  override def toCompact(devonIterator: Iterator[Devon]): String =
    compactFormatter.format(devonIterator)

  override def toPretty(devon: Devon): Seq[String] =
    prettyFormatter.format(devon)

  override def fromValue[T: universe.TypeTag](value: T): Devon =
    devonReflection.fromValue(value)

  override def toValue[T: universe.TypeTag](devon: Devon, theClass: Class[T]): T =
    devonReflection.toValue(devon, theClass)
}
