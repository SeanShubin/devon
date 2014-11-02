package com.seanshubin.devon.core

import org.scalatest.FunSuite

class TokenizerRulesTest extends FunSuite {
  test("two simple words") {
    val text = "ab cd"
    val charIterator = text.toIterator
    val cursorEnd0 = Cursor.fromIterator(charIterator)
    val MatchSuccess(cursorStart1, cursorEnd1) = TokenizerRules.matchToken(cursorEnd0)
    val MatchSuccess(cursorStart2, cursorEnd2) = TokenizerRules.matchToken(cursorEnd1)
    val MatchSuccess(cursorStart3, cursorEnd3) = TokenizerRules.matchToken(cursorEnd2)
    val MatchSuccess(cursorStart4, cursorEnd4) = TokenizerRules.matchEnd(cursorEnd3)
    assert(Cursor.values(cursorStart1, cursorEnd1) === Seq('a', 'b'))
    assert(Cursor.values(cursorStart2, cursorEnd2) === Seq(' '))
    assert(Cursor.values(cursorStart3, cursorEnd3) === Seq('c', 'd'))
    assert(Cursor.values(cursorStart4, cursorEnd4) === Seq())
    assert(cursorEnd4.isEnd === true)
  }
  test("unexpected end") {
    val text = ""
    val charIterator = text.toIterator
    val cursor = Cursor.fromIterator(charIterator)
    val result = TokenizerRules.matchToken(cursor)
    assert(result === MatchFail("word or whitespace-block expected"))
  }
  test("expected end but didn't get it") {
    val text = "foo"
    val charIterator = text.toIterator
    val cursor = Cursor.fromIterator(charIterator)
    val result = TokenizerRules.matchEnd(cursor)
    assert(result === MatchFail("end of input expected"))
  }
}
