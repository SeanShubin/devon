package com.seanshubin.devon.core

class TokenParser extends Parser[Char, Token] {
  override def parse(ruleName: String, cursor: Cursor[Char], assembler: Assembler[Char, Token]): MatchResult[Char, Token] = ???
}
