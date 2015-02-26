package com.seanshubin.devon.core

import com.seanshubin.devon.core.devon._
import org.scalatest.FunSuite

class DevonMarshallerTest extends FunSuite {
  test("elements") {
    val parsed = new DefaultDevonMarshaller().stringToIterator("a b c").toSeq
    assert(parsed === Seq(DevonString("a"), DevonString("b"), DevonString("c")))
  }

  test("map") {
    val element = new DefaultDevonMarshaller().fromString("{a b c d}")
    assert(element === DevonMap(Map(
      DevonString("a") -> DevonString("b"),
      DevonString("c") -> DevonString("d")
    )))
  }

  test("array") {
    val element = new DefaultDevonMarshaller().fromString("[a b c]")
    assert(element === DevonArray(Seq(
      DevonString("a"),
      DevonString("b"),
      DevonString("c"))))
  }

  test("string") {
    val element = new DefaultDevonMarshaller().fromString("abc")
    assert(element === DevonString("abc"))
  }

  test("string with spaces") {
    val element = new DefaultDevonMarshaller().fromString("'a b'")
    assert(element === DevonString("a b"))
  }

  test("string with single quotes") {
    val element = new DefaultDevonMarshaller().fromString("'a''b'")
    assert(element === DevonString("a'b"))
  }

  test("null") {
    val element = new DefaultDevonMarshaller().fromString("()")
    assert(element === DevonNull)
  }

  test("text to iterator") {
    val text = "a {b c} () [d]"
    val devonMarshaller = new DefaultDevonMarshaller()
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
    val devonMarshaller = new DefaultDevonMarshaller()
    val devon = devonMarshaller.fromString(text)
    assert(devon === DevonMap(Map(DevonString("a") -> DevonString("b"))))
  }

  test("to compact") {
    val devon = DevonMap(Map(DevonString("a") -> DevonString("b")))
    val devonMarshaller = new DefaultDevonMarshaller()
    val compact = devonMarshaller.toCompact(devon)
    assert(compact === "{a b}")
  }

  test("to pretty") {
    val devon = DevonMap(Map(DevonString("a") -> DevonString("b")))
    val devonMarshaller = new DefaultDevonMarshaller()
    val prettyLines = devonMarshaller.toPretty(devon)
    assert(prettyLines.size === 3)
    assert(prettyLines(0) === "{")
    assert(prettyLines(1) === "  a b")
    assert(prettyLines(2) === "}")
  }

  test("from value") {
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val rectangle = Rectangle(topLeft, bottomRight)
    val devonMarshaller = new DefaultDevonMarshaller()
    val devon = devonMarshaller.fromValue(rectangle)
    val compact = devonMarshaller.toCompact(devon)
    assert(compact === "{topLeft{x 1 y 2}bottomRight{x 3 y 4}}")
  }

  test("to value") {
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val expectedRectangle = Rectangle(topLeft, bottomRight)
    val compact = "{topLeft{x 1 y 2}bottomRight{x 3 y 4}}"
    val devonMarshaller = new DefaultDevonMarshaller()
    val devon = devonMarshaller.fromString(compact)
    val actualRectangle = devonMarshaller.toValue(devon, classOf[Rectangle])
    assert(actualRectangle === expectedRectangle)
  }

  test("composite from value") {
    val composite = SampleWithCompositeTypes(Map(1 -> "a"), Seq(Point(2, 3)))
    val devonMarshaller = new DefaultDevonMarshaller()
    val devon = devonMarshaller.fromValue(composite)
    val compact = devonMarshaller.toCompact(devon)
    assert(compact === "{sampleMap{1 a}sampleSeq[{x 2 y 3}]}")
  }

  test("composite to value") {
    val expected = SampleWithCompositeTypes(Map(1 -> "a"), Seq(Point(2, 3)))
    val compact = "{sampleMap{1 a}sampleSeq[{x 2 y 3}]}"
    val devonMarshaller = new DefaultDevonMarshaller()
    val devon = devonMarshaller.fromString(compact)
    val actual = devonMarshaller.toValue(devon, classOf[SampleWithCompositeTypes])
    assert(actual === expected)
  }
}
