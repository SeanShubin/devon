package com.seanshubin.devon.prototype

trait RuleMatcher[A] {
  def applyRule(ruleName:String, cursor:Cursor[A]):MatchResult[A]
}
