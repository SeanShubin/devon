package com.seanshubin.devon.core

object TokenizerRules extends Rules[Char] {
  def matchWhitespace(cursor: Cursor[Char]): MatchResult[Char] = {
    val result = matchChar(cursor, ' ')
    result
  }

  def matchWhitespaceBlock(cursor: Cursor[Char]): MatchResult[Char] = {
    val result = matchOneOrMore(cursor, matchWhitespace)
    result
  }

  def matchWordChar(cursor: Cursor[Char]): MatchResult[Char] = {
    val result = matchAnyCharExcept(cursor, ' ')
    result
  }

  def matchWord(cursor: Cursor[Char]): MatchResult[Char] = {
    val result = matchOneOrMore(cursor, matchWordChar) match {
      case MatchSuccess(_, newCursor) =>
        MatchSuccess(cursor, newCursor)
      case x => x
    }
    result
  }

  def matchToken(cursor: Cursor[Char]): MatchResult[Char] = {
    val result = matchOneOf(cursor, "word or whitespace-block expected", matchWord, matchWhitespaceBlock)
    result
  }
}
