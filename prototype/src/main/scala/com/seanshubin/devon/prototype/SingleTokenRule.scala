package com.seanshubin.devon.prototype

class SingleTokenRule[A, B](token:A) extends Rule[A, B] {
  override def apply(ruleLookup: RuleLookup[A, B], cursor: Cursor[A], assembler: Assembler[A, B]): MatchResult[A, B] = {
    val ruleName: String = "single token"
    if(cursor.isEnd) MatchFail(ruleName, s"expected $token but got end of input")
    else if(cursor.value == token) MatchSuccess(ruleName, cursor, cursor.next, assembler.update(ruleName, cursor, cursor.next))
    else MatchFail(ruleName, s"expected $token but got ${cursor.value}")
  }
}
