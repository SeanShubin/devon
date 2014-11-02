package com.seanshubin.devon.prototype

sealed trait Token

case object TokenOpenBrace extends Token

case object TokenCloseBrace extends Token

case object TokenOpenBracket extends Token

case object TokenCloseBracket extends Token

case object TokenOpenParen extends Token

case object TokenCloseParen extends Token

case object TokenNull extends Token

case class TokenWord(value: String) extends Token
