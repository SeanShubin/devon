package com.seanshubin.devon.core.rules

import com.seanshubin.devon.core._

case class ValueOtherThanRule[A, B](ruleLookup: RuleLookup[A], thisRuleName: String, forbiddenValues: A*) extends Rule[A] {
  override def apply(cursor: Cursor[A]): MatchResult[A] = {
    if (cursor.isEnd) MatchFailure("end of input")
    else {
      if (forbiddenValues.contains(cursor.value)) {
        MatchFailure(s"${cursor.value} is forbidden here")
      } else {
        MatchSuccess(ParseTreeLeaf(thisRuleName, cursor.value), cursor.next)
      }
    }
  }
}
