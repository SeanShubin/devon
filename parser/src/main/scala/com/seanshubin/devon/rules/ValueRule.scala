package com.seanshubin.devon.rules

import com.seanshubin.devon.parser._

case class ValueRule[A](ruleLookup: RuleLookup[A], thisRuleName: String, values: A*) extends Rule[A] {
  override def apply(cursor: Cursor[A]): MatchResult[A] = {
    if (cursor.isEnd) MatchFailure(thisRuleName, "end of input")
    else {
      if (values.contains(cursor.value)) {
        MatchSuccess(ParseTreeLeaf(thisRuleName, cursor.value), cursor.next)
      } else {
        val valueString = StringUtil.seqToString(values)
        MatchFailure(thisRuleName, s"Expected one of: $valueString, but got ${cursor.value} instead")
      }
    }
  }
}
