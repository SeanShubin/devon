package com.seanshubin.devon.core

import org.scalatest.FunSuite

class TokenizerRulesTest extends FunSuite {
  test("two simple words") {
    val text = "ab cd"
    val charIterator = text.toIterator
    val cursor = Cursor.fromIterator(charIterator)
    val tokenRuleLookup = new TokenRuleLookup()
    val parser = new ParserImpl(tokenRuleLookup)
    val emptyAssembler = TokenAssembler.Empty
    val result1 = parser.parse("unquoted-word", cursor, emptyAssembler)
    val MatchSuccess(endCursor1, assembler1, children1) = result1
    assert(assembler1.value === TokenWord("ab"))
    val result2 = parser.parse("unquoted-word", cursor, emptyAssembler)
    val MatchSuccess(endCursor2, assembler2, children2) = result2
    assert(assembler2.value === TokenWord("cd"))
  }
}
