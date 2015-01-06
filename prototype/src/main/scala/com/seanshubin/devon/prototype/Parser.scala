package com.seanshubin.devon.prototype

trait Parser[A, B] {
  def parse(ruleName: String, cursor: Cursor[A], assembler: Assembler[A, B]): Either[String, B]
}
