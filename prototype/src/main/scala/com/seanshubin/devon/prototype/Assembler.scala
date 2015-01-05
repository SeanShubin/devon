package com.seanshubin.devon.prototype

trait Assembler[A,B] {
  type AssembleCommand = (String, A) => Assembler[A, B]
  def top:B
  def push(value:A):Assembler[A,B]
}
