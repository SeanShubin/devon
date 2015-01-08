package com.seanshubin.devon.core

sealed trait MatchResult[A]

case class MatchSuccess[A](parseTree: ParseTree[A], cursor: Cursor[A]) extends MatchResult[A]

case class MatchFailure[A](message: String) extends MatchResult[A]
