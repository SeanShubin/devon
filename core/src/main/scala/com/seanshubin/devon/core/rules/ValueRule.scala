package com.seanshubin.devon.core.rules

import com.seanshubin.devon.core._

case class ValueRule[A, B](thisRuleName: String, ruleLookup: RuleLookup[A], value: A) extends Rule[A] {
  override def apply(cursor: Cursor[A]): MatchResult[A] = {
    if (cursor.isEnd) MatchFailure("end of input")
    else {
      if (value == cursor.value) {
        MatchSuccess(ParseTreeLeaf(thisRuleName, cursor.value), cursor.next)
      } else {
        MatchFailure(s"$value expected, but got ${cursor.value}")
      }
    }
  }
}
