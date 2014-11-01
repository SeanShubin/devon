package com.seanshubin.devon.core

trait Rules {
  final def matchChar(cursor: Cursor[Char], ch: Char): MatchResult = {
    val result =
      if (cursor.isEnd) MatchFail
      else if (cursor.value == ch) MatchSuccess(cursor.next, Nil)
      else MatchFail
    result
  }

  final def matchOneOrMore(cursor: Cursor[Char], f: Cursor[Char] => MatchResult): MatchResult = {
    val result = f(cursor) match {
      case MatchSuccess(newCursor, _) => matchZeroOrMore(newCursor, f)
      case MatchFail => MatchFail
    }
    result
  }

  final def matchZeroOrMore(cursor: Cursor[Char], f: Cursor[Char] => MatchResult): MatchResult = {
    val result = f(cursor) match {
      case MatchSuccess(newCursor, _) => matchZeroOrMore(newCursor, f)
      case MatchFail => MatchSuccess(cursor, Nil)
    }
    result
  }

  final def matchNotChar(cursor: Cursor[Char], ch: Char): MatchResult = {
    val result =
      if (cursor.isEnd) MatchFail
      else if (cursor.value == ch) MatchFail
      else MatchSuccess(cursor.next, Nil)
    result
  }

  final def matchOneOf(cursor: Cursor[Char], functions: (Cursor[Char] => MatchResult)*): MatchResult = {
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

  final def matchEnd(cursor: Cursor[Char]): MatchResult = {
    val result =
      if (cursor.isEnd) MatchSuccess(cursor, Nil)
      else MatchFail
    result
  }
}
