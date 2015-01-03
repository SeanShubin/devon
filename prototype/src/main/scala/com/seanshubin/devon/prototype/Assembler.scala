package com.seanshubin.devon.prototype

trait Assembler[A,B] {
  type AssembleCommand = (String, A) => Assembler[A, B]
  def top:B
}
