package com.seanshubin.devon.core

sealed trait MatchResult[A, B] {
  def isSuccess: Boolean
}

case class MatchSuccess[A, B](assembler: Assembler[A, B]) extends MatchResult[A, B] {
  override def isSuccess: Boolean = true
}
