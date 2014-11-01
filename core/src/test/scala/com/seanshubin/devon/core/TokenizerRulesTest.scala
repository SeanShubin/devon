package com.seanshubin.devon.core

import org.scalatest.FunSuite

class TokenizerRulesTest extends FunSuite {
  test("two simple words") {
    val text = "ab cd"
    val charIterator = text.toIterator
    val cursor0 = Cursor.fromIterator(charIterator)
    val MatchSuccess(cursor1, result1) = TokenizerRules.matchToken(cursor0)
    val MatchSuccess(cursor2, result2) = TokenizerRules.matchToken(cursor1)
    val MatchSuccess(cursor3, result3) = TokenizerRules.matchToken(cursor2)
    val MatchSuccess(cursor4, result4) = TokenizerRules.matchEnd(cursor3)
    assert(result1 === Seq(TokenWord("ab")))
    assert(result2 === Seq())
    assert(result3 === Seq(TokenWord("cd")))
    assert(result4 === Seq())
    assert(cursor4.isEnd === true)
  }
}
