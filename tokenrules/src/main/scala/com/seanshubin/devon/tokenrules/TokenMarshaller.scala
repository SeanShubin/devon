package com.seanshubin.devon.tokenrules

trait TokenMarshaller {
  def charsToIterator(charIterator: Iterator[Char]): Iterator[Token]

  def stringToIterator(text: String): Iterator[Token]
}
