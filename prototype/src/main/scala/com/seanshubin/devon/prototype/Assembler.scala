package com.seanshubin.devon.prototype

trait Assembler[A, B] {
  def top: B

  def update(ruleName: String, cursorBegin: Cursor[A], cursorEnd: Cursor[A]): Assembler[A, B]
}
