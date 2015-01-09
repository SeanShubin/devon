package com.seanshubin.devon.core

import org.scalatest.FunSuite

class TokenizerRulesTest extends FunSuite {
  test("two simple words") {
    val text = "ab cd"
    val charIterator = text.toIterator
    val cursor = Cursor.fromIterator(charIterator)
    val tokenRuleLookup = new TokenRuleLookup()
    val parser = new ParserImpl(tokenRuleLookup)

    val result1 = parser.parse("unquoted-word", cursor)
    val MatchSuccess(parseTree1, cursor1) = result1
    val ParseTreeBranch(name1, children1) = parseTree1
    assert(name1 === "unquoted-word")
    assert(children1 === Seq(ParseTreeLeaf("unquoted-word-character", 'a'), ParseTreeLeaf("unquoted-word-character", 'b')))

    val result2 = parser.parse("unquoted-word", cursor1.next)
    val MatchSuccess(parseTree2, _) = result2
    val ParseTreeBranch(name2, children2) = parseTree2
    assert(name2 === "unquoted-word")
    assert(children2 === Seq(ParseTreeLeaf("unquoted-word-character", 'c'), ParseTreeLeaf("unquoted-word-character", 'd')))
  }
}
