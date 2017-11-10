package com.seanshubin.devon.domain

import com.seanshubin.devon.tokenizer.Token

trait DevonIteratorFactory {
  def stringToIterator(text: String): Iterator[Devon]

  def charsToIterator(charIterator: Iterator[Char]): Iterator[Devon]

  def tokensToIterator(unfilteredTokenIterator: Iterator[Token]): Iterator[Devon]
}
