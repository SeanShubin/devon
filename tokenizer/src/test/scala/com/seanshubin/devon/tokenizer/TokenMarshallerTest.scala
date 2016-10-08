package com.seanshubin.devon.tokenizer

import com.seanshubin.devon.parser.NoOperationStringProcessor
import org.scalatest.FunSuite

class TokenMarshallerTest extends FunSuite {
  val tokenMarshaller: TokenMarshaller = new TokenMarshallerImpl(NoOperationStringProcessor)
  test("braces") {
    val tokens = tokenMarshaller.stringToIterator("{}").toSeq
    assert(tokens === Seq(TokenOpenBrace, TokenCloseBrace))
  }
  test("brackets") {
    val tokens = tokenMarshaller.stringToIterator("[]").toSeq
    assert(tokens === Seq(TokenOpenBracket, TokenCloseBracket))
  }
  test("null") {
    val tokens = tokenMarshaller.stringToIterator("()").toSeq
    assert(tokens === Seq(TokenNull))
  }
  test("whitespace") {
    val tokens = tokenMarshaller.stringToIterator(" \t\r\n").toSeq
    assert(tokens === Seq(TokenWhitespace(" \t\r\n")))
  }
  test("string") {
    val tokens = tokenMarshaller.stringToIterator("abc").toSeq
    assert(tokens === Seq(TokenString("abc")))
  }
  test("string with spaces") {
    val tokens = tokenMarshaller.stringToIterator("'a b'").toSeq
    assert(tokens === Seq(TokenString("a b")))
  }
  test("string with single quotes") {
    val tokens = tokenMarshaller.stringToIterator("'a''b'").toSeq
    assert(tokens === Seq(TokenString("a'b")))
  }
}
