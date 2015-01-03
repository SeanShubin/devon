package com.seanshubin.devon.prototype

class SequenceRule[A, B](ruleNames: String*) extends Rule[A, B] {
  override def apply(ruleLookup: RuleLookup[A, B], cursor: Cursor[A], assembler: Assembler[A, B]): MatchResult[A, B] = {
    val firstRule = ruleLookup.lookupRuleByName(ruleNames.head)
    val firstResult = firstRule.apply(ruleLookup, cursor, assembler)
    def loop(lastResult:MatchResult[A,B], remainingRules:List[String]): MatchResult[A, B] = {
      lastResult match {
        case x:MatchSuccess[A,B] =>
          val rule = ruleLookup.lookupRuleByName(remainingRules.head)
          val result = rule.apply(ruleLookup, x.end, x.assembler)
          loop(result, remainingRules.tail)
        case x:MatchFail[A,B]  => x
      }
    }
    val result = loop(firstResult, ruleNames.toList.tail)
    result
  }
}
