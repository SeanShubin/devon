package com.seanshubin.devon.tokenizer

sealed trait Token

case class TokenString(word: String) extends Token

case class TokenWhitespace(whitespace: String) extends Token

case object TokenOpenBrace extends Token

case object TokenCloseBrace extends Token

case object TokenOpenBracket extends Token

case object TokenCloseBracket extends Token

case object TokenNull extends Token

case object TokenEnd extends Token
