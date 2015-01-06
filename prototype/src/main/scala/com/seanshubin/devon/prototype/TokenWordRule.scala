package com.seanshubin.devon.prototype

class TokenWordRule[A, B]() extends Rule[A, B] {
  override def apply(ruleLookup: RuleLookup[A, B], cursor: Cursor[A], assembler: Assembler[A, B]): MatchResult[A, B] = {
    val ruleName: String = "string"
    val result: MatchResult[A, B] =
      if (cursor.isEnd) MatchFail(ruleName, s"expected $ruleName but got end of input")
      else cursor.value match {
        case x: TokenWord => MatchSuccess(ruleName, cursor, cursor.next, assembler.update(ruleName, cursor, cursor.next))
        case _ => MatchFail(ruleName, s"expected $ruleName but got ${cursor.value}")
      }
    result
  }
}
