package com.seanshubin.devon.core

import com.seanshubin.devon.core.devon._
import org.scalatest.FunSuite

class DevonIteratorTest extends FunSuite {
  def createElement(value: String): Devon = {
    val parsed = DevonIterator.fromString(value).toSeq
    assert(parsed.size === 1)
    parsed.head
  }

  test("elements") {
    val parsed = DevonIterator.fromString("a b c").toSeq
    assert(parsed === Seq(DevonString("a"), DevonString("b"), DevonString("c")))
  }
  test("map") {
    val element = createElement("{a b c d}")
    assert(element === DevonMap(Map(
      DevonString("a") -> DevonString("b"),
      DevonString("c") -> DevonString("d")
    )))
  }
  test("array") {
    val element = createElement("[a b c]")
    assert(element === DevonArray(Seq(
      DevonString("a"),
      DevonString("b"),
      DevonString("c"))))
  }
  test("string") {
    val element = createElement("abc")
    assert(element === DevonString("abc"))
  }
  test("string with spaces") {
    val element = createElement("'a b'")
    assert(element === DevonString("a b"))
  }
  test("string with single quotes") {
    val element = createElement("'a''b'")
    assert(element === DevonString("a'b"))
  }
  test("null") {
    val element = createElement("()")
    assert(element === DevonNull)
  }
}
