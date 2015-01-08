package com.seanshubin.devon.core

sealed trait MatchResult[A, B]

case class MatchSuccess[A, B](endCursor: Cursor[A], assembler: Assembler[A, B], childMatches: List[MatchResult[A, B]]) extends MatchResult[A, B]

case class MatchFailure[A, B](message: String) extends MatchResult[A, B]
