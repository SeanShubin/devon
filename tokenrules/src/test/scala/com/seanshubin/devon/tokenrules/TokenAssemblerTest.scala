package com.seanshubin.devon.tokenrules

import com.seanshubin.devon.parser.{NoOperationStringProcessor, ParseTreeBranch, ParseTreeLeaf}
import org.scalatest.FunSuite

class TokenAssemblerTest extends FunSuite {
  test("assemble word") {
    val a = ParseTreeLeaf("not-space", 'a')
    val b = ParseTreeLeaf("not-space", 'b')
    val parseTree = ParseTreeBranch("unquoted-string", List(a, b))
    val tokenAssembler = new TokenAssembler(NoOperationStringProcessor)
    val actualToken = tokenAssembler.assembleFromParseTree(parseTree)
    val expectedToken = TokenString("ab")
    assert(actualToken === expectedToken)
  }
}
