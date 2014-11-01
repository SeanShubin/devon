package com.seanshubin.devon.core

import org.scalatest.FunSuite

class CursorTest extends FunSuite {
  test("from iterator") {
    val text = "abc"
    val iterator = text.iterator
    val cursorA = Cursor.fromIterator(iterator)
    val cursorB = cursorA.next
    val cursorC = cursorB.next
    val cursorEnd = cursorC.next
    assert(cursorA.value === 'a')
    assert(cursorA.isEnd === false)
    assert(cursorB.value === 'b')
    assert(cursorB.isEnd === false)
    assert(cursorC.value === 'c')
    assert(cursorC.isEnd === false)
    assert(cursorEnd.isEnd === true)
  }

  test("split cursors iterator") {
    val text = "abc"
    val iterator = text.iterator
    val cursorA = Cursor.fromIterator(iterator)
    val cursorB = cursorA.next
    val cursorC = cursorA.next
    assert(cursorA.value === 'a')
    assert(cursorA.isEnd === false)
    assert(cursorB.value === 'b')
    assert(cursorB.isEnd === false)
    assert(cursorC.value === 'b')
    assert(cursorC.isEnd === false)
  }

  test("compute between") {
    val text = "abcde"
    val iterator = text.iterator
    val cursorA = Cursor.fromIterator(iterator)
    val cursorB = cursorA.next
    val cursorE = cursorB.next.next.next
    val actual = Cursor.values(cursorB, cursorE)
    val expected = Seq('b', 'c', 'd')
    assert(actual === expected)
  }
}
