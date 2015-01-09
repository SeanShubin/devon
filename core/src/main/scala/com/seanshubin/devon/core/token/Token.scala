package com.seanshubin.devon.core.token

import com.seanshubin.devon.core.StringUtil

sealed trait Token

case class TokenWord(word: String) extends Token

case class TokenWhitespaceBlock(whitespace: String) extends Token {
  override def toString: String = s"TokenWhitespaceBlock(${StringUtil.escape(whitespace)})"
}

case object TokenOpenBrace extends Token

case object TokenCloseBrace extends Token

case object TokenOpenBracket extends Token

case object TokenCloseBracket extends Token

case object TokenNull extends Token
