package com.seanshubin.devon.core.token

import com.seanshubin.devon.parser._

class TokenAssembler(stringProcessor: StringProcessor) extends Assembler[Char, Token] {
  override def assembleFromParseTree(parseTree: ParseTree[Char]): Token = {
    assemblers(parseTree.name)(parseTree)
  }

  private def extractUnquotedWord(parseTree: ParseTree[Char]): String = {
    val ParseTreeBranch(name, children) = parseTree
    val unprocessedString = children.map(extractUnquotedValue).mkString
    val processedString = stringProcessor.processedToRaw(unprocessedString)
    processedString
  }

  private def extractUnquotedValue(parseTree: ParseTree[Char]): Char = {
    val ParseTreeLeaf(name, value) = parseTree
    value
  }

  private def extractQuotedWord(parseTree: ParseTree[Char]): String = {
    val ParseTreeBranch("quoted-string", List(_, ParseTreeBranch("quoted-contents", quotedContents), _)) = parseTree
    val unprocessedString = quotedContents.map(extractQuotedChar).mkString
    val processedString = stringProcessor.processedToRaw(unprocessedString)
    processedString
  }

  private def extractQuotedChar(parseTree: ParseTree[Char]): Char = {
    parseTree match {
      case ParseTreeLeaf("not-quote", value) => value
      case ParseTreeBranch("two-quotes", _) => '\''
      case _ => ???
    }
  }

  private val assemblers: Map[String, ParseTree[Char] => Token] = Map(
    "unquoted-string" -> (parseTree => TokenString(extractUnquotedWord(parseTree))),
    "quoted-string" -> (parseTree => TokenString(extractQuotedWord(parseTree))),
    "whitespace-block" -> (parseTree => TokenWhitespace(extractUnquotedWord(parseTree))),
    "open-brace" -> (parseTree => TokenOpenBrace),
    "close-brace" -> (parseTree => TokenCloseBrace),
    "open-bracket" -> (parseTree => TokenOpenBracket),
    "close-bracket" -> (parseTree => TokenCloseBracket),
    "null" -> (parseTree => TokenNull),
    "end" -> (parseTree => TokenEnd)
  )
}
