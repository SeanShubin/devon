package com.seanshubin.devon.core

sealed trait MatchResult[A] {
  def success:Boolean
}

case class MatchSuccess[A](parseTree: ParseTree[A], cursor: Cursor[A]) extends MatchResult[A] {
  override def success: Boolean = true
}

case class MatchFailure[A](message: String) extends MatchResult[A] {
  override def success: Boolean = false
}
