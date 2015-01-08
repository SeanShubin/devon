package com.seanshubin.devon.core

class ParserImpl[A](ruleLookup: RuleLookup[A]) extends Parser[A] {
  override def parse(ruleName: String, cursor: Cursor[A]): MatchResult[A] = {
    val rule: Rule[A] = ruleLookup.lookupRuleByName(ruleName)
    val matchResult: MatchResult[A] = rule.apply(cursor)
    matchResult
  }
}
