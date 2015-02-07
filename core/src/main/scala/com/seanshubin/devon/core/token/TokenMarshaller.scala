package com.seanshubin.devon.core.token

trait TokenMarshaller {
  def charsToIterator(charIterator: Iterator[Char]): Iterator[Token]
}
