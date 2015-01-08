package com.seanshubin.devon.core

case class TokenAssembler(values: List[Token]) extends Assembler[Char, Token] {
  override def value: Token = values.head

  override def successfulMatch(ruleName: String, begin: Cursor[Char], afterEnd: Cursor[Char]): Assembler[Char, Token] = {
    println(ruleName)
    if (ruleName == "word") {
      val text = Cursor.values(begin, afterEnd).mkString
      TokenAssembler(TokenWord(text) :: values)
    } else {
      this
    }
  }
}

object TokenAssembler {
  val Empty = TokenAssembler(Nil)
}
