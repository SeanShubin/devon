package com.seanshubin.devon.prototype

class OneOfRule[A, B](ruleNames: String*) extends Rule[A, B] {
  override def apply(ruleLookup: RuleLookup[A, B], cursor: Cursor[A], assembler: Assembler[A, B]): MatchResult[A, B] = {
    def applyByName(ruleName: String): MatchResult[A, B] = {
      val rule = ruleLookup.lookupRuleByName(ruleName)
      val result = rule.apply(ruleLookup, cursor, assembler)
      result
    }
    val results = ruleNames.toStream.map(applyByName)
    val result = results.find(_.isSuccess) match {
      case Some(x) => x
      case None => results.head
    }
    result
  }
}
