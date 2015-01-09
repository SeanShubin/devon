package com.seanshubin.devon.core

class TokenIterator(backingIterator: Iterator[Char]) extends Iterator[Token] {
  private val ruleLookup = new TokenRuleLookup()
  private val parser = new ParserImpl(ruleLookup)
  private val assembler = new TokenAssembler
  private var currentCursor = Cursor.fromIterator(backingIterator)
  private var currentMatchResult = parser.parse("token", currentCursor)

  override def hasNext: Boolean = currentMatchResult.success

  override def next(): Token = {
    val MatchSuccess(parseTree, newCursor) = currentMatchResult
    val token = assembler.assembleFromParseTree(parseTree)
    currentCursor = newCursor
    currentMatchResult = parser.parse("token", currentCursor)
    token
  }
}

object TokenIterator {
  def fromString(text: String): Iterator[Token] = {
    new TokenIterator(text.toIterator)
  }
}
