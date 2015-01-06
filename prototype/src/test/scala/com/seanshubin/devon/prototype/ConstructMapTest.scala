package com.seanshubin.devon.prototype

import org.scalatest.FunSuite

class ConstructMapTest extends FunSuite {
  test("construct map") {
    val tokens: Seq[Token] = Seq(
      TokenOpenBrace,
      TokenWord("ab"), TokenWord("cd"),
      TokenWord("ef"), TokenWord("gh"),
      TokenCloseBrace)
    val tokenCursor:Cursor[Token] = Cursor.fromIterator(tokens.toIterator)
    val ruleLookup:RuleLookup[Token, AbstractSyntaxTree] = new ParserRuleLookup()
    val parser:Parser[Token, AbstractSyntaxTree] = new ParserImpl(ruleLookup)
    val assembler:Assembler[Token, AbstractSyntaxTree] = new ElementAssembler(Nil)
    val actual:Either[String, AbstractSyntaxTree] = parser.parse("element", tokenCursor, assembler)
    val expected:Either[String, AbstractSyntaxTree] = Right(AstObject(Map(AstString("ab") -> AstString("cd"), AstString("ef") -> AstString("gh"))))
    assert(actual === expected)
  }
}
