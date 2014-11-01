package com.seanshubin.devon.core

import org.scalatest.FunSuite

class TokenizerRulesTest extends FunSuite {
  test("two simple words") {
    val text = "ab cd"
    val charIterator = text.toIterator
    val cursor0 = Cursor.fromIterator(charIterator)
    val MatchSuccess(cursor1) = TokenizerRules.matchToken(cursor0)
    val MatchSuccess(cursor2) = TokenizerRules.matchToken(cursor1)
    val MatchSuccess(cursor3) = TokenizerRules.matchToken(cursor2)
    val MatchSuccess(cursor4) = TokenizerRules.matchEnd(cursor3)
    assert(Cursor.values(cursor0, cursor1) === Seq('a', 'b'))
    assert(Cursor.values(cursor1, cursor2) === Seq(' '))
    assert(Cursor.values(cursor2, cursor3) === Seq('c', 'd'))
    assert(Cursor.values(cursor3, cursor4) === Seq())
    assert(cursor4.isEnd === true)
  }
}
