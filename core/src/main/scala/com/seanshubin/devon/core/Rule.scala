package com.seanshubin.devon.core

trait Rule[A, B] {
  def apply(thisRuleName: String, ruleLookup: RuleLookup[A, B], cursor: Cursor[A], assembler: Assembler[A, B]): MatchResult[A, B]
}
