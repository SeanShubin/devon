package com.seanshubin.devon.parser

sealed trait MatchResult[A] {
  def success: Boolean
}

case class MatchSuccess[A](parseTree: ParseTree[A], cursor: Cursor[A]) extends MatchResult[A] {
  override def success: Boolean = true
}

case class MatchFailure[A](rule: String, message: String) extends MatchResult[A] {
  override def success: Boolean = false
}
