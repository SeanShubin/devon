package com.seanshubin.devon.prototype

class ParserImpl[A,B](ruleLookup:RuleLookup[A,B]) extends Parser[A,B] {
  def parse(originalRuleName:String, cursor:Cursor[A], originalAssembler:Assembler[A, B]):Either[String, B] = {
    val rule:Rule[A,B] = ruleLookup.lookupRuleByName(originalRuleName)
    val matchResult:MatchResult[A,B] = rule.apply(ruleLookup, cursor, originalAssembler)
    matchResult match {
      case MatchSuccess(ruleName, start, end, newAssembler) => Right(newAssembler.top)
      case MatchFail(ruleName, message) => Left(message)
    }
  }
}
