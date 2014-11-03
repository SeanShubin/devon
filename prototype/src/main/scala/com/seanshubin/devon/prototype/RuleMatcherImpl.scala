package com.seanshubin.devon.prototype

class RuleMatcherImpl[A](ruleLookup:RuleLookup[A]) extends RuleMatcher[A] {
  override def applyRule(ruleName: String, cursor: Cursor[A]): MatchResult[A] = {
    val rule = ruleLookup.lookupByName(ruleName)
    rule.apply(ruleLookup, cursor)
  }
}
