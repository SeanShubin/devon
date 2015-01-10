package com.seanshubin.devon.core.rules

import com.seanshubin.devon.core._

case class EndRule[A](ruleLookup: RuleLookup[A], thisRuleName: String) extends Rule[A] {
  override def apply(cursor: Cursor[A]): MatchResult[A] = {
    if (cursor.isEnd) MatchSuccess(ParseTreeBranch(thisRuleName, Nil), cursor)
    else MatchFailure(thisRuleName, "end of input expected")
  }
}
