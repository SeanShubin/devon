package com.seanshubin.devon.core

import com.seanshubin.devon.core.token._
import org.scalatest.FunSuite

class TokenMarshallerTest extends FunSuite {
  test("braces") {
    val tokens = new TokenMarshallerImpl().stringToIterator("{}").toSeq
    assert(tokens === Seq(TokenOpenBrace, TokenCloseBrace))
  }
  test("brackets") {
    val tokens = new TokenMarshallerImpl().stringToIterator("[]").toSeq
    assert(tokens === Seq(TokenOpenBracket, TokenCloseBracket))
  }
  test("null") {
    val tokens = new TokenMarshallerImpl().stringToIterator("()").toSeq
    assert(tokens === Seq(TokenNull))
  }
  test("whitespace") {
    val tokens = new TokenMarshallerImpl().stringToIterator(" \t\r\n").toSeq
    assert(tokens === Seq(TokenWhitespace(" \t\r\n")))
  }
  test("string") {
    val tokens = new TokenMarshallerImpl().stringToIterator("abc").toSeq
    assert(tokens === Seq(TokenString("abc")))
  }
  test("string with spaces") {
    val tokens = new TokenMarshallerImpl().stringToIterator("'a b'").toSeq
    assert(tokens === Seq(TokenString("a b")))
  }
  test("string with single quotes") {
    val tokens = new TokenMarshallerImpl().stringToIterator("'a''b'").toSeq
    assert(tokens === Seq(TokenString("a'b")))
  }
}
