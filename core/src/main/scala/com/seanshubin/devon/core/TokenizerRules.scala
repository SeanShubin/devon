package com.seanshubin.devon.core

object TokenizerRules extends Rules {
  def matchWhitespace(cursor: Cursor[Char]): MatchResult = {
    val result = matchChar(cursor, ' ')
    result
  }

  def matchWhitespaceBlock(cursor: Cursor[Char]): MatchResult = {
    val result = matchOneOrMore(cursor, matchWhitespace)
    result
  }

  def matchWordChar(cursor: Cursor[Char]): MatchResult = {
    val result = matchNotChar(cursor, ' ')
    result
  }

  def matchWord(cursor: Cursor[Char]): MatchResult = {
    val result = matchOneOrMore(cursor, matchWordChar) match {
      case MatchSuccess(newCursor, _) =>
        val wordText = Cursor.values(cursor, newCursor).mkString
        val wordToken = TokenWord(wordText)
        MatchSuccess(newCursor, Seq(wordToken))
      case x => x
    }
    result
  }

  def matchToken(cursor: Cursor[Char]): MatchResult = {
    val result = matchOneOf(cursor, matchWord, matchWhitespaceBlock)
    result
  }
}
