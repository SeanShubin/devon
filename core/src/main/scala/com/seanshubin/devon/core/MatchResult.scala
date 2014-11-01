package com.seanshubin.devon.core

sealed trait MatchResult

case class MatchSuccess(cursor: Cursor[Char], tokens: Seq[Token]) extends MatchResult

case object MatchFail extends MatchResult
