package com.seanshubin.devon.core

trait Assembler[A,B] {
  def value:B
  def successfulMatch(ruleName:String, begin:Cursor[A], afterEnd:Cursor[A]):Assembler[A,B]
}
