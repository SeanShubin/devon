package com.seanshubin.devon.core

trait Parser[A] {
  def parse(ruleName: String, cursor: Cursor[A]): MatchResult[A]
}
