package com.seanshubin.devon.core

import com.seanshubin.devon.core.devon._
import org.scalatest.FunSuite

class DevonMarshallerTest extends FunSuite {
  test("elements") {
    val parsed = new DevonMarshallerImpl().stringToIterator("a b c").toSeq
    assert(parsed === Seq(DevonString("a"), DevonString("b"), DevonString("c")))
  }

  test("map") {
    val element = new DevonMarshallerImpl().fromString("{a b c d}")
    assert(element === DevonMap(Map(
      DevonString("a") -> DevonString("b"),
      DevonString("c") -> DevonString("d")
    )))
  }

  test("array") {
    val element = new DevonMarshallerImpl().fromString("[a b c]")
    assert(element === DevonArray(Seq(
      DevonString("a"),
      DevonString("b"),
      DevonString("c"))))
  }

  test("string") {
    val element = new DevonMarshallerImpl().fromString("abc")
    assert(element === DevonString("abc"))
  }

  test("string with spaces") {
    val element = new DevonMarshallerImpl().fromString("'a b'")
    assert(element === DevonString("a b"))
  }

  test("string with single quotes") {
    val element = new DevonMarshallerImpl().fromString("'a''b'")
    assert(element === DevonString("a'b"))
  }

  test("null") {
    val element = new DevonMarshallerImpl().fromString("()")
    assert(element === DevonNull)
  }

  test("text to iterator") {
    val text = "a {b c} () [d]"
    val devonMarshaller = new DevonMarshallerImpl()
    val iterator = devonMarshaller.stringToIterator(text)
    val values = iterator.toSeq
    assert(values.size === 4)
    assert(values(0) === DevonString("a"))
    assert(values(1) === DevonMap(Map(DevonString("b") -> DevonString("c"))))
    assert(values(2) === DevonNull)
    assert(values(3) === DevonArray(Seq(DevonString("d"))))
  }

  test("text to abstract syntax tree") {
    val text = "{a b}"
    val devonMarshaller = new DevonMarshallerImpl()
    val devon = devonMarshaller.fromString(text)
    assert(devon === DevonMap(Map(DevonString("a") -> DevonString("b"))))
  }

  test("to compact") {
    val devon = DevonMap(Map(DevonString("a") -> DevonString("b")))
    val devonMarshaller = new DevonMarshallerImpl()
    val compact = devonMarshaller.toCompact(devon)
    assert(compact === "{a b}")
  }

  test("to pretty") {
    val devon = DevonMap(Map(DevonString("a") -> DevonString("b")))
    val devonMarshaller = new DevonMarshallerImpl()
    val prettyLines = devonMarshaller.toPretty(devon)
    assert(prettyLines.size === 3)
    assert(prettyLines(0) === "{")
    assert(prettyLines(1) === "  a b")
    assert(prettyLines(2) === "}")
  }

  ignore("from value") {
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val rectangle = Rectangle(topLeft, bottomRight)
    val devonMarshaller = new DevonMarshallerImpl()
    val devon = devonMarshaller.fromValue(rectangle)
    val compact = devonMarshaller.toCompact(devon)
    assert(compact === "{topLeft{x 1 y 2}bottomRight{x 3 y 4}}")
  }
}
