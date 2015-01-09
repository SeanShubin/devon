package com.seanshubin.devon.core.rules

import com.seanshubin.devon.core._

case class OneOfRule[A](thisRuleName: String, ruleLookup: RuleLookup[A], ruleNames: String*) extends Rule[A] {
  override def apply(cursor: Cursor[A]): MatchResult[A] = {
    val successfulMatchFromCursor = successfulMatch(_: String, cursor)

    ruleNames.toStream.map(successfulMatchFromCursor).flatten.headOption match {
      case Some(matchResult) => matchResult
      case None => failure()
    }
  }

  private def successfulMatch(ruleName: String, cursor: Cursor[A]): Option[MatchSuccess[A]] = {
    val rule = ruleLookup.lookupRuleByName(ruleName)
    val matchResult = rule.apply(cursor)
    matchResult match {
      case x: MatchSuccess[A] => Some(x)
      case x: MatchFailure[A] => None
    }
  }

  private def failure(): MatchResult[A] = {
    val ruleNamesString = ruleNames.mkString(", ")
    MatchFailure(s"expected one of $ruleNamesString")
  }
}
