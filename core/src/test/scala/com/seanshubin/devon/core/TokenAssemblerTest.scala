package com.seanshubin.devon.core

import com.seanshubin.devon.core.token.{TokenAssembler, TokenWord}
import org.scalatest.FunSuite

class TokenAssemblerTest extends FunSuite {
  test("assemble word") {
    val a = ParseTreeLeaf("not-space", 'a')
    val b = ParseTreeLeaf("not-space", 'b')
    val parseTree = ParseTreeBranch("unquoted-word", List(a, b))
    val tokenAssembler = new TokenAssembler()
    val actualToken = tokenAssembler.assembleFromParseTree(parseTree)
    val expectedToken = TokenWord("ab")
    assert(actualToken === expectedToken)
  }
}
