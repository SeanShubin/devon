package com.seanshubin.devon.core.rules

import com.seanshubin.devon.core._

case class OneOrMoreRule[A](ruleLookup: RuleLookup[A], thisRuleName: String, ruleName: String) extends Rule[A] {
  private lazy val rule = ruleLookup.lookupRuleByName(ruleName)

  override def apply(cursor: Cursor[A]): MatchResult[A] = {
    val firstResult = rule.apply(cursor)
    firstResult match {
      case x: MatchSuccess[A] => applyAfterFirst(x)
      case x: MatchFailure[A] => firstResult
    }
  }

  private def applyAfterFirst(firstMatch: MatchSuccess[A]): MatchSuccess[A] = {
    val matches: List[MatchSuccess[A]] = applyRemaining(List(firstMatch))
    val childParseTrees = matches.map(_.parseTree)
    val parseTree = ParseTreeBranch(thisRuleName, childParseTrees)
    MatchSuccess(parseTree, matches.last.cursor)
  }

  private def applyRemaining(resultsSoFar: List[MatchSuccess[A]]): List[MatchSuccess[A]] = {
    val nextResult = rule.apply(resultsSoFar.head.cursor)
    nextResult match {
      case x: MatchSuccess[A] => applyRemaining(x :: resultsSoFar)
      case x: MatchFailure[A] => resultsSoFar.reverse
    }
  }
}
