package com.seanshubin.devon.core

trait Rules[ItemType] {
  final def matchChar(cursor: Cursor[ItemType], ch: ItemType): MatchResult[ItemType] = {
    val result: MatchResult[ItemType] =
      if (cursor.isEnd) MatchFail("unexpected end of input")
      else if (cursor.value == ch) MatchSuccess(cursor.next, Nil)
      else MatchFail(s"expected $ch, but got ${cursor.next}")
    result
  }

  final def matchOneOrMore(cursor: Cursor[ItemType], f: Cursor[ItemType] => MatchResult[ItemType]): MatchResult[ItemType] = {
    val result = f(cursor) match {
      case MatchSuccess(newCursor, _) => matchZeroOrMore(newCursor, f)
      case x => x
    }
    result
  }

  final def matchZeroOrMore(cursor: Cursor[ItemType], f: Cursor[ItemType] => MatchResult[ItemType]): MatchResult[ItemType] = {
    val result = f(cursor) match {
      case MatchSuccess(newCursor, _) => matchZeroOrMore(newCursor, f)
      case _ => MatchSuccess(cursor, Nil)
    }
    result
  }

  final def matchNotChar(cursor: Cursor[ItemType], ch: ItemType): MatchResult[ItemType] = {
    val result: MatchResult[ItemType] =
      if (cursor.isEnd) MatchFail("unexpected end of input")
      else if (cursor.value == ch) MatchFail(s"expected not $ch")
      else MatchSuccess(cursor.next, Nil)
    result
  }

  final def matchOneOf(cursor: Cursor[ItemType], message: String, functions: (Cursor[ItemType] => MatchResult[ItemType])*): MatchResult[ItemType] = {
    val result: MatchResult[ItemType] =
      if (functions.isEmpty) MatchFail(message)
      else {
        val headFunction = functions.head
        val headFunctionResult = headFunction(cursor)
        headFunctionResult match {
          case x: MatchSuccess[ItemType] => x
          case _ => matchOneOf(cursor, message, functions.tail: _*)
        }
      }
    result
  }

  final def matchEnd(cursor: Cursor[ItemType]): MatchResult[ItemType] = {
    val result: MatchResult[ItemType] =
      if (cursor.isEnd) MatchSuccess[ItemType](cursor, Nil)
      else MatchFail("end of input expected")
    result
  }
}
