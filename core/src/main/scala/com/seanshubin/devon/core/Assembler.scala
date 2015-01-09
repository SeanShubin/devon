package com.seanshubin.devon.core

trait Assembler[A, B] {
  def assembleFromParseTree(parseTree: ParseTree[A]): B
}
