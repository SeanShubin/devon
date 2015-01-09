package com.seanshubin.devon.core

class TokenAssembler extends Assembler[Char, Token] {
  override def assembleFromParseTree(parseTree: ParseTree[Char]): Token = {
    assemblers(parseTree.name)(parseTree)
  }

  private def unquotedWord(parseTree: ParseTree[Char]): Token = {
    val ParseTreeBranch(name, children) = parseTree
    TokenWord(children.map(extractValue).mkString)
  }

  private def extractValue(parseTree: ParseTree[Char]): Char = {
    val ParseTreeLeaf(name, value) = parseTree
    value
  }

  private val assemblers: Map[String, ParseTree[Char] => Token] = Map(
    "unquoted-word" -> unquotedWord
  )
}
