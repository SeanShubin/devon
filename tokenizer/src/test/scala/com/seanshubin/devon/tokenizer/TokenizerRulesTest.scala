package com.seanshubin.devon.tokenizer

import com.seanshubin.devon.parser.{Cursor, MatchSuccess, ParseTreeBranch, ParseTreeLeaf}
import org.scalatest.FunSuite

class TokenizerRulesTest extends FunSuite {
  test("two simple words") {
    val text = "ab cd"
    val charIterator = text.toIterator
    val cursor = Cursor.fromIterator(charIterator)
    val tokenRuleLookup = new TokenRuleLookup()
    val rule = tokenRuleLookup.lookupRuleByName("unquoted-string")

    val result1 = rule.apply(cursor)
    val MatchSuccess(parseTree1, cursor1) = result1
    val ParseTreeBranch(name1, children1) = parseTree1
    assert(name1 === "unquoted-string")
    assert(children1 === Seq(ParseTreeLeaf("unquoted-string-character", 'a'), ParseTreeLeaf("unquoted-string-character", 'b')))

    val result2 = rule.apply(cursor1.next)
    val MatchSuccess(parseTree2, _) = result2
    val ParseTreeBranch(name2, children2) = parseTree2
    assert(name2 === "unquoted-string")
    assert(children2 === Seq(ParseTreeLeaf("unquoted-string-character", 'c'), ParseTreeLeaf("unquoted-string-character", 'd')))
  }
}
