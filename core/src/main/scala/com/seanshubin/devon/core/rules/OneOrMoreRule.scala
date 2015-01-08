package com.seanshubin.devon.core.rules

import com.seanshubin.devon.core._

case class OneOrMoreRule[A, B](ruleName: String) extends Rule[A, B] {
  override def apply(thisRuleName: String, ruleLookup: RuleLookup[A, B], cursor: Cursor[A], assembler: Assembler[A, B]): MatchResult[A, B] = {
    val rule = ruleLookup.lookupRuleByName(ruleName)
    val firstResult = rule.apply(ruleName, ruleLookup, cursor, assembler)
    firstResult match {
      case x: MatchSuccess[A, B] => applyRemaining(ruleName, ruleLookup, rule, x)
      case x: MatchFailure[A, B] => firstResult
    }
  }

  private def applyRemaining(ruleName: String, ruleLookup: RuleLookup[A, B], rule: Rule[A, B], resultSoFar: MatchSuccess[A, B]): MatchResult[A, B] = {
    val nextResult = rule.apply(ruleName, ruleLookup, resultSoFar.endCursor, resultSoFar.assembler)
    nextResult match {
      case x: MatchSuccess[A, B] => applyRemaining(ruleName, ruleLookup, rule, x)
      case x: MatchFailure[A, B] => resultSoFar
    }
  }
}
