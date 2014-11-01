package com.seanshubin.devon.core

sealed trait MatchResult[ItemType]

case class MatchSuccess[ItemType](cursor: Cursor[ItemType], tokens: Seq[Token]) extends MatchResult[ItemType]

case class MatchFail[ItemType](message: String) extends MatchResult[ItemType]
