package com.seanshubin.devon.core

trait Parser[A, B] {
  def parse(ruleName: String, cursor: Cursor[A], assembler: Assembler[A, B]): MatchResult[A,B]
}
