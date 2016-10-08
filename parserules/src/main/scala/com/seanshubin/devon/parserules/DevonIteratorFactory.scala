package com.seanshubin.devon.parserules

import com.seanshubin.devon.tokenrules.Token

trait DevonIteratorFactory {
  def stringToIterator(text: String): Iterator[Devon]

  def charsToIterator(charIterator: Iterator[Char]): Iterator[Devon]

  def tokensToIterator(unfilteredTokenIterator: Iterator[Token]): Iterator[Devon]
}
