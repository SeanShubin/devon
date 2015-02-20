package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.token.Token

trait DevonIteratorFactory {
  def stringToIterator(text: String): Iterator[Devon]

  def charsToIterator(charIterator: Iterator[Char]): Iterator[Devon]

  def tokensToIterator(unfilteredTokenIterator: Iterator[Token]): Iterator[Devon]
}
