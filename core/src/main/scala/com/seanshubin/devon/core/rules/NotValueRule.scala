package com.seanshubin.devon.core.rules

import com.seanshubin.devon.core._

case class NotValueRule[A, B](forbiddenValues: Seq[A]) extends Rule[A, B] {
  override def apply(thisRuleName: String, ruleLookup: RuleLookup[A, B], cursor: Cursor[A], assembler: Assembler[A, B]): MatchResult[A, B] = {
    if (cursor.isEnd) MatchFailure("end of input")
    else {
      if (forbiddenValues.contains(cursor.value)) {
        MatchFailure(s"${cursor.value} is forbidden here")
      } else {
        MatchSuccess(cursor.next, assembler.successfulMatch(thisRuleName, cursor, cursor.next), Nil)
      }
    }
  }
}
