package com.seanshubin.devon.prototype

class ParserImpl[A,B](ruleLookup:RuleLookup[A,B]) extends Parser[A,B] {
  def parse(ruleName:String, cursor:Cursor[A], assembler:Assembler[A, B]):Either[String, B] = {
    val rule:Rule[A,B] = ruleLookup.lookupRuleByName(ruleName)
    val matchResult:MatchResult[A,B] = rule.apply(ruleLookup, cursor, assembler)
    matchResult match {
      case MatchSuccess(ruleName, start, end, assembler) => Right(assembler.top)
      case MatchFail(ruleName, message) => Left(message)

    }
  }
}
