package com.seanshubin.devon.core.token

import com.seanshubin.devon.core.{Assembler, ParseTree, ParseTreeBranch, ParseTreeLeaf}

class TokenAssembler extends Assembler[Char, Token] {
  override def assembleFromParseTree(parseTree: ParseTree[Char]): Token = {
    assemblers(parseTree.name)(parseTree)
  }

  private def extractUnquotedWord(parseTree: ParseTree[Char]): String = {
    val ParseTreeBranch(name, children) = parseTree
    children.map(extractUnquotedValue).mkString
  }

  private def extractUnquotedValue(parseTree: ParseTree[Char]): Char = {
    val ParseTreeLeaf(name, value) = parseTree
    value
  }

  private def extractQuotedWord(parseTree: ParseTree[Char]): String = {
    val ParseTreeBranch("quoted-word", List(_, ParseTreeBranch("quoted-contents", quotedContents), _)) = parseTree
    quotedContents.map(extractQuotedChar).mkString
  }

  private def extractQuotedChar(parseTree: ParseTree[Char]): Char = {
    parseTree match {
      case ParseTreeLeaf("not-quote", value) => value
      case ParseTreeBranch("two-quotes", _) => '\''
      case _ => ???
    }
  }

  private val assemblers: Map[String, ParseTree[Char] => Token] = Map(
    "unquoted-word" -> (parseTree => TokenWord(extractUnquotedWord(parseTree))),
    "quoted-word" -> (parseTree => TokenWord(extractQuotedWord(parseTree))),
    "whitespace-block" -> (parseTree => TokenWhitespaceBlock(extractUnquotedWord(parseTree))),
    "open-brace" -> (parseTree => TokenOpenBrace),
    "close-brace" -> (parseTree => TokenCloseBrace),
    "open-bracket" -> (parseTree => TokenOpenBracket),
    "close-bracket" -> (parseTree => TokenCloseBracket),
    "null" -> (parseTree => TokenNull)
  )
}
