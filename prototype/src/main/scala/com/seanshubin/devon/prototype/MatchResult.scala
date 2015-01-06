package com.seanshubin.devon.prototype

sealed trait MatchResult[A, B] {
  def ruleName: String

  def combine(that: MatchResult[A, B]): MatchResult[A, B]

  def withRuleName(ruleName: String): MatchResult[A, B]

  def isSuccess: Boolean
}

case class MatchSuccess[A, B](ruleName: String, start: Cursor[A], end: Cursor[A], assembler: Assembler[A, B]) extends MatchResult[A, B] {
  override def combine(that: MatchResult[A, B]): MatchResult[A, B] = {
    that match {
      case x: MatchSuccess[A, B] => combineSuccess(x)
      case x: MatchFail[A, B] => x
    }
  }

  override def withRuleName(newRuleName: String): MatchResult[A, B] = copy(ruleName = newRuleName)

  override def isSuccess: Boolean = true

  def combineSuccess(that: MatchSuccess[A, B]): MatchSuccess[A, B] = {
    MatchSuccess[A, B](ruleName, start, that.end, that.assembler)
  }
}

case class MatchFail[A, B](ruleName: String, message: String) extends MatchResult[A, B] {
  override def combine(that: MatchResult[A, B]): MatchResult[A, B] = ???

  override def withRuleName(newRuleName: String): MatchResult[A, B] = copy(ruleName = newRuleName)

  override def isSuccess: Boolean = false
}
