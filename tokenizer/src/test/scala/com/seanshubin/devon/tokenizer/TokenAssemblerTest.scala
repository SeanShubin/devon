package com.seanshubin.devon.tokenizer

import com.seanshubin.devon.parser.{ParseTreeBranch, ParseTreeLeaf, StringProcessor}
import org.scalatest.FunSuite

class TokenAssemblerTest extends FunSuite {
  test("assemble word") {
    val a = ParseTreeLeaf("not-space", 'a')
    val b = ParseTreeLeaf("not-space", 'b')
    val parseTree = ParseTreeBranch("unquoted-string", List(a, b))
    val tokenAssembler = new TokenAssembler(new StringProcessor {
      override def processedToRaw(s: String): String = s

      override def rawToProcessed(s: String): String = s
    })
    val actualToken = tokenAssembler.assembleFromParseTree(parseTree)
    val expectedToken = TokenString("ab")
    assert(actualToken === expectedToken)
  }
}
