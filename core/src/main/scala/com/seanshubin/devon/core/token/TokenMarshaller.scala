package com.seanshubin.devon.core.token

trait TokenMarshaller {
  def charsToIterator(charIterator: Iterator[Char]): Iterator[Token]

  def stringToIterator(text: String): Iterator[Token]
}
