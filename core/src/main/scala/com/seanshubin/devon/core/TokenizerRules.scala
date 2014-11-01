package com.seanshubin.devon.core

object TokenizerRules {
  def matchChar(cursor: Cursor[Char], ch: Char): MatchResult = {
    val result =
      if (cursor.isEnd) MatchFail
      else if (cursor.value == ch) MatchSuccess(cursor.next, Nil)
      else MatchFail
    result
  }

  def matchWhitespace(cursor: Cursor[Char]): MatchResult = {
    val result = matchChar(cursor, ' ')
    result
  }

  def matchWhitespaceBlock(cursor: Cursor[Char]): MatchResult = {
    val result = matchOneOrMore(cursor, matchWhitespace)
    result
  }

  def matchOneOrMore(cursor: Cursor[Char], f: Cursor[Char] => MatchResult): MatchResult = {
    val result = f(cursor) match {
      case MatchSuccess(newCursor, _) => matchZeroOrMore(newCursor, f)
      case MatchFail => MatchFail
    }
    result
  }

  def matchZeroOrMore(cursor: Cursor[Char], f: Cursor[Char] => MatchResult): MatchResult = {
    val result = f(cursor) match {
      case MatchSuccess(newCursor, _) => matchZeroOrMore(newCursor, f)
      case MatchFail => MatchSuccess(cursor, Nil)
    }
    result
  }

  def matchNotChar(cursor: Cursor[Char], ch: Char): MatchResult = {
    val result =
      if (cursor.isEnd) MatchFail
      else if (cursor.value == ch) MatchFail
      else MatchSuccess(cursor.next, Nil)
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

  def matchOneOf(cursor: Cursor[Char], functions: (Cursor[Char] => MatchResult)*): MatchResult = {
    val result =
      if (functions.isEmpty) MatchFail
      else {
        val headFunction = functions.head
        val headFunctionResult = headFunction(cursor)
        headFunctionResult match {
          case x: MatchSuccess => x
          case _ => matchOneOf(cursor, functions.tail: _*)
        }
      }
    result
  }

  def matchEnd(cursor: Cursor[Char]): MatchResult = {
    val result =
      if (cursor.isEnd) MatchSuccess(cursor, Nil)
      else MatchFail
    result
  }
}
