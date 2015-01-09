package com.seanshubin.devon.core

trait Rule[A] {
  def thisRuleName: String

  def apply(cursor: Cursor[A]): MatchResult[A]
}
