package com.seanshubin.devon.core

sealed trait MatchResult[A]

case class MatchSuccess[A](start: Cursor[A], end: Cursor[A]) extends MatchResult[A]

case class MatchFail[A](message: String) extends MatchResult[A]
