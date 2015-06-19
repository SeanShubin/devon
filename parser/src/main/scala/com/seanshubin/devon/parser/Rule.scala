package com.seanshubin.devon.parser

trait Rule[A] {
  def thisRuleName: String

  def apply(cursor: Cursor[A]): MatchResult[A]
}
