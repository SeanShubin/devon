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
    val result = matchNotChar(cursor, ' ')
    result
  }

  def matchWord(cursor: Cursor[Char]): MatchResult[Char] = {
    val result = matchOneOrMore(cursor, matchWordChar) match {
      case MatchSuccess(newCursor, _) =>
        val wordText = Cursor.values(cursor, newCursor).mkString
        val wordToken = TokenWord(wordText)
        MatchSuccess(newCursor, Seq(wordToken))
      case x => x
    }
    result
  }

  def matchToken(cursor: Cursor[Char]): MatchResult[Char] = {
    val result = matchOneOf(cursor, "word or whitespace-block expected", matchWord, matchWhitespaceBlock)
    result
  }
}
