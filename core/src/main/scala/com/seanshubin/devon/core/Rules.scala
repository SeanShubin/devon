package com.seanshubin.devon.core

trait Rules[A] {
  final def matchChar(cursor: Cursor[A], ch: A): MatchResult[A] = {
    val result: MatchResult[A] =
      if (cursor.isEnd) MatchFail("unexpected end of input")
      else if (cursor.value == ch) MatchSuccess(cursor, cursor.next)
      else MatchFail(s"expected $ch, but got ${cursor.value}")
    result
  }

  final def matchOneOrMore(cursor0: Cursor[A], f: Cursor[A] => MatchResult[A]): MatchResult[A] = {
    val result = f(cursor0) match {
      case MatchSuccess(_, cursor1) =>
        matchZeroOrMore(cursor1, f) match {
          case MatchSuccess(_, cursor2) => MatchSuccess(cursor0, cursor2)
          case x => x
        }
      case x => x
    }
    result
  }

  final def matchZeroOrMore(cursor: Cursor[A], f: Cursor[A] => MatchResult[A]): MatchResult[A] = {
    val firstMatch = f(cursor)
    firstMatch match {
      case MatchSuccess(startCursor, endCursor) =>
        val MatchSuccess(_, newEndCursor) = matchZeroOrMore(endCursor, f)
        MatchSuccess(cursor, newEndCursor)
      case _ => MatchSuccess(cursor, cursor)
    }
  }

  final def matchAnyCharExcept(cursor: Cursor[A], ch: A): MatchResult[A] = {
    val result: MatchResult[A] =
      if (cursor.isEnd) MatchFail("unexpected end of input")
      else if (cursor.value == ch) MatchFail(s"expected not $ch")
      else MatchSuccess(cursor, cursor.next)
    result
  }

  final def matchOneOf(cursor: Cursor[A], message: String, functions: (Cursor[A] => MatchResult[A])*): MatchResult[A] = {
    val result: MatchResult[A] =
      if (functions.isEmpty) MatchFail(message)
      else {
        val headFunction = functions.head
        val headFunctionResult = headFunction(cursor)
        headFunctionResult match {
          case x: MatchSuccess[A] => x
          case _ => matchOneOf(cursor, message, functions.tail: _*)
        }
      }
    result
  }

  final def matchEnd(cursor: Cursor[A]): MatchResult[A] = {
    val result: MatchResult[A] =
      if (cursor.isEnd) MatchSuccess[A](cursor, cursor)
      else MatchFail("end of input expected")
    result
  }
}
