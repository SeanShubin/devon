package com.seanshubin.devon.prototype

class ZeroOrMoreRule[A, B](ruleName: String) extends Rule[A, B] {
  override def apply(ruleLookup: RuleLookup[A, B], cursor: Cursor[A], assembler: Assembler[A, B]): MatchResult[A, B] = {
    val rule = ruleLookup.lookupRuleByName(ruleName)
    val firstResult = rule.apply(ruleLookup, cursor, assembler)
    def loop(theCursor: Cursor[A], oldParseResult: MatchSuccess[A, B]): MatchResult[A, B] = {
      val newParseResult = rule.apply(ruleLookup, theCursor, oldParseResult.assembler)
      newParseResult match {
        case x: MatchSuccess[A, B] =>
          loop(x.end, oldParseResult.combine(x.ruleName, newParseResult).asInstanceOf[MatchSuccess[A, B]])
        case x: MatchFail[A, B] => oldParseResult
      }
    }
    val result =
      firstResult match {
        case x: MatchSuccess[A, B] =>
          loop(x.end, x)
        case x: MatchFail[A, B] =>
          MatchSuccess(ruleName, cursor, cursor, assembler)
      }
    val combinedResult = result.withRuleName(ruleName)
    combinedResult
  }
}
