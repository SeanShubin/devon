package com.seanshubin.devon.parser

trait Assembler[A, B] {
  def assembleFromParseTree(parseTree: ParseTree[A]): B
}
