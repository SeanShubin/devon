package com.seanshubin.devon.core

import com.seanshubin.devon.core.token._
import org.scalatest.FunSuite

class TokenIteratorTest extends FunSuite {
  test("braces") {
    val tokens = TokenIterator.fromString("{}").toSeq
    assert(tokens === Seq(TokenOpenBrace, TokenCloseBrace))
  }
  test("brackets") {
    val tokens = TokenIterator.fromString("[]").toSeq
    assert(tokens === Seq(TokenOpenBracket, TokenCloseBracket))
  }
  test("null") {
    val tokens = TokenIterator.fromString("()").toSeq
    assert(tokens === Seq(TokenNull))
  }
  test("whitespace") {
    val tokens = TokenIterator.fromString(" \t\r\n").toSeq
    assert(tokens === Seq(TokenWhitespace(" \t\r\n")))
  }
  test("string") {
    val tokens = TokenIterator.fromString("abc").toSeq
    assert(tokens === Seq(TokenString("abc")))
  }
  test("string with spaces") {
    val tokens = TokenIterator.fromString("'a b'").toSeq
    assert(tokens === Seq(TokenString("a b")))
  }
  test("string with single quotes") {
    val tokens = TokenIterator.fromString("'a''b'").toSeq
    assert(tokens === Seq(TokenString("a'b")))
  }
}
