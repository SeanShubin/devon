package com.seanshubin.devon.prototype

trait Rule[A] {
  def apply(ruleLookup:RuleLookup[A], cursor:Cursor[A]):MatchResult[A]
}
