package com.seanshubin.devon.core

class ParserImpl[A, B](ruleLookup: RuleLookup[A, B]) extends Parser[A, B] {
  override def parse(ruleName: String, cursor: Cursor[A], assembler: Assembler[A, B]): MatchResult[A, B] = {
    val rule: Rule[A, B] = ruleLookup.lookupRuleByName(ruleName)
    val matchResult: MatchResult[A, B] = rule.apply(ruleName, ruleLookup, cursor, assembler)
    matchResult
  }
}
