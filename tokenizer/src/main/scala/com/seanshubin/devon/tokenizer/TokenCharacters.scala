package com.seanshubin.devon.tokenizer

object TokenCharacters {
  val quote = '\''
  val openBrace = '{'
  val closeBrace = '}'
  val openBracket = '['
  val closeBracket = ']'
  val openParen = '('
  val closeParen = ')'
  val space = ' '
  val tab = '\t'
  val carriageReturn = '\r'
  val lineFeed = '\n'

  val whitespace = Seq(space, tab, carriageReturn, lineFeed)
  val structural = Seq(openBrace, closeBrace, openBracket, closeBracket, openParen, closeParen, quote)
  val structuralAndWhitespace = structural ++ whitespace
}
