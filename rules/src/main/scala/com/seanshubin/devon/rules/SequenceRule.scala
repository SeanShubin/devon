package com.seanshubin.devon.rules

import com.seanshubin.devon.parser._

case class SequenceRule[A](ruleLookup: RuleLookup[A], thisRuleName: String, ruleNames: String*) extends Rule[A] {
  override def apply(cursor: Cursor[A]): MatchResult[A] = {
    def loop(soFar: List[MatchSuccess[A]], remaining: List[String], cursor: Cursor[A]): List[MatchResult[A]] = {
      if (remaining.isEmpty) soFar
      else {
        val rule = ruleLookup.lookupRuleByName(remaining.head)
        val matchResult = rule.apply(cursor)
        matchResult match {
          case x: MatchSuccess[A] => loop(x :: soFar, remaining.tail, x.cursor)
          case x: MatchFailure[A] => List(x)
        }
      }
    }
    val allResults = loop(Nil, ruleNames.toList, cursor)
    allResults.head match {
      case MatchSuccess(_, newCursor) =>
        val children = allResults.flatMap {
          case MatchSuccess(parseTree, _) => Some(parseTree)
          case _ => None
        }
        MatchSuccess(ParseTreeBranch(thisRuleName, children.reverse), newCursor)
      case x: MatchFailure[A] =>
        val sequenceString = ruleNames.mkString(", ")
        MatchFailure(thisRuleName, s"expected sequence: $sequenceString")
    }
  }
}
