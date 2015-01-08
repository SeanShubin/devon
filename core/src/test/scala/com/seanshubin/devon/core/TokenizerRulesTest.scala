package com.seanshubin.devon.core

import org.scalatest.FunSuite

class TokenizerRulesTest extends FunSuite {
  test("two simple words") {
    val text = "ab cd"
    val charIterator = text.toIterator
    val cursor = Cursor.fromIterator(charIterator)
    val tokenRuleLookup = new TokenRuleLookup()
    val parser = new TokenParser()
    val emptyAssembler = TokenAssembler.Empty
    val result1 = parser.parse("element", cursor, emptyAssembler)
    assert(result1.isSuccess === true)
    val MatchSuccess(assembler1) = result1
    assert(assembler1.value === TokenWord("ab"))
    val result2 = parser.parse("element", cursor, emptyAssembler)
    assert(result2.isSuccess === true)
    val MatchSuccess(assembler2) = result2
    assert(assembler2.value === TokenWord("cd"))
  }
}
