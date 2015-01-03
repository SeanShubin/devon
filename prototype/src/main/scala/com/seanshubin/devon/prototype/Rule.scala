package com.seanshubin.devon.prototype

trait Rule[A, B] {
  def apply(ruleLookup:RuleLookup[A,B], cursor:Cursor[A], assembler:Assembler[A, B]):MatchResult[A,B]
}
