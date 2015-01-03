package com.seanshubin.devon.prototype

sealed trait MatchResult[A, B] {
  def ruleName: String

  def combine(newRule: String, that: MatchResult[A, B]): MatchResult[A, B]

  def withRuleName(ruleName: String): MatchResult[A, B]

  def isSuccess:Boolean
}

case class MatchSuccess[A, B](ruleName: String, start: Cursor[A], end: Cursor[A], assembler: Assembler[A, B]) extends MatchResult[A, B] {
  override def combine(newRule: String, that: MatchResult[A, B]): MatchResult[A, B] = ???

  override def withRuleName(newRuleName: String): MatchResult[A, B] = copy(ruleName = newRuleName)

  override def isSuccess: Boolean = true
}

case class MatchFail[A, B](ruleName: String, message: String) extends MatchResult[A, B] {
  override def combine(newRule: String, that: MatchResult[A, B]): MatchResult[A, B] = ???

  override def withRuleName(newRuleName: String): MatchResult[A, B] = copy(ruleName = newRuleName)

  override def isSuccess: Boolean = false
}
