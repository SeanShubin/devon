package com.seanshubin.devon.core

sealed trait MatchResult[A] {
  def success: Boolean
}

case class MatchSuccess[A](parseTree: ParseTree[A], cursor: Cursor[A]) extends MatchResult[A] {
  override def success: Boolean = true
}

case class MatchFailure[A](message: String, maybeCause: Option[MatchFailure[A]]) extends MatchResult[A] {
  override def success: Boolean = false
}

object MatchFailure {
  def apply[A](message: String) = new MatchFailure[A](message, None)
}
