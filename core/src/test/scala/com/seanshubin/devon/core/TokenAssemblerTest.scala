package com.seanshubin.devon.core

import org.scalatest.FunSuite

class TokenAssemblerTest extends FunSuite {
  test("assemble word") {
    val text = "ab"
    val cursorA = Cursor.fromIterator(text.toIterator)
    val cursorB = cursorA.next
    val cursorEnd = cursorB.next
    val tokenAssembler =
      TokenAssembler.Empty.
        successfulMatch("word-char", cursorA, cursorB).
        successfulMatch("word-char", cursorB, cursorEnd).
        successfulMatch("word", cursorA, cursorEnd)
    assert(tokenAssembler.value === TokenWord("ab"))
  }
}
