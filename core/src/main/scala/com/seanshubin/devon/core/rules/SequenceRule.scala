package com.seanshubin.devon.core.rules

import com.seanshubin.devon.core._

case class SequenceRule[A](thisRuleName: String, ruleLookup: RuleLookup[A], ruleNames: String*) extends Rule[A] {
  override def apply(cursor: Cursor[A]): MatchResult[A] = {
    ???
  }
}
