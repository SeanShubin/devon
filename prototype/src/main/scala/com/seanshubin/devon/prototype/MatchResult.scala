package com.seanshubin.devon.prototype

sealed trait MatchResult[A] {
  def ruleName:String
}

case class MatchSuccess[A](ruleName:String, start: Cursor[A], end: Cursor[A]) extends MatchResult[A]

case class MatchFail[A](ruleName:String, message: String) extends MatchResult[A]
