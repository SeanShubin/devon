package com.seanshubin.devon.domain

import com.seanshubin.devon.reflection.{ReflectionImpl, SimpleTypeConversion}
import com.seanshubin.devon.tokenizer.TokenMarshallerImpl
import org.scalatest.FunSuite

class DevonMarshallerTest extends FunSuite {
  test("elements") {
    val parsed = DevonMarshallerWiring.Default.stringToIterator("a b c").toSeq
    assert(parsed === Seq(DevonString("a"), DevonString("b"), DevonString("c")))
  }

  test("map") {
    val element = DevonMarshallerWiring.Default.fromString("{a b c d}")
    assert(element === DevonMap(Map(
      DevonString("a") -> DevonString("b"),
      DevonString("c") -> DevonString("d")
    )))
  }

  test("array") {
    val element = DevonMarshallerWiring.Default.fromString("[a b c]")
    assert(element === DevonArray(Seq(
      DevonString("a"),
      DevonString("b"),
      DevonString("c"))))
  }

  test("string") {
    val element = DevonMarshallerWiring.Default.fromString("abc")
    assert(element === DevonString("abc"))
  }

  test("string with spaces") {
    val element = DevonMarshallerWiring.Default.fromString("'a b'")
    assert(element === DevonString("a b"))
  }

  test("string with single quotes") {
    val element = DevonMarshallerWiring.Default.fromString("'a''b'")
    assert(element === DevonString("a'b"))
  }

  test("null") {
    val element = DevonMarshallerWiring.Default.fromString("()")
    assert(element === DevonNull)
  }

  test("text to iterator") {
    val text = "a {b c} () [d]"
    val devonMarshaller = DevonMarshallerWiring.Default
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
    val devonMarshaller = DevonMarshallerWiring.Default
    val devon = devonMarshaller.fromString(text)
    assert(devon === DevonMap(Map(DevonString("a") -> DevonString("b"))))
  }

  test("compact") {
    val arrayOfArray: Seq[Seq[String]] = Seq(Seq("a"), Seq("b c"))
    val arrayOfMap: Seq[Map[String, String]] = Seq(Map("a" -> "b c"))
    val mapOfArray: Map[Seq[String], Seq[String]] = Map(Seq("a", "b c") -> Seq("d e", "f"))
    val mapOfMap: Map[Map[String, String], Map[String, String]] = Map(Map("a" -> "b c") -> Map("d e" -> "f"))
    val strings: Seq[String] = Seq("a", "b c", "d e", "f", "g")
    val marshaller = DevonMarshallerWiring.Default
    assert(marshaller.valueToCompact(arrayOfArray) === "[[a]['b c']]")
    assert(marshaller.valueToCompact(arrayOfMap) === "[{a'b c'}]")
    assert(marshaller.valueToCompact(mapOfArray) === "{[a'b c']['d e'f]}")
    assert(marshaller.valueToCompact(mapOfMap) === "{{a'b c'}{'d e'f}}")
    assert(marshaller.valueToCompact(strings) === "[a'b c' 'd e'f g]")
  }

  test("pretty") {
    val arrayOfArray: Seq[Seq[String]] = Seq(Seq("a"), Seq("b c"))
    val arrayOfMap: Seq[Map[String, String]] = Seq(Map("a" -> "b c"))
    val mapOfArray: Map[Seq[String], Seq[String]] = Map(Seq("a", "b c") -> Seq("d e", "f"))
    val mapOfMap: Map[Map[String, String], Map[String, String]] = Map(Map("a" -> "b c") -> Map("d e" -> "f"))
    val strings: Seq[String] = Seq("a", "b c", "d e", "f", "g")
    val marshaller = DevonMarshallerWiring.Default
    linesEqual(marshaller.valueToPretty(arrayOfArray),
      """[
        |  [
        |    a
        |  ]
        |  [
        |    'b c'
        |  ]
        |]""".stripMargin)
    linesEqual(marshaller.valueToPretty(arrayOfMap),
      """[
        |  {
        |    a 'b c'
        |  }
        |]""".stripMargin)
    linesEqual(marshaller.valueToPretty(mapOfArray),
      """{
        |  [
        |    a
        |    'b c'
        |  ]
        |  [
        |    'd e'
        |    f
        |  ]
        |}""".stripMargin)
    linesEqual(marshaller.valueToPretty(mapOfMap),
      """{
        |  {
        |    a 'b c'
        |  }
        |  {
        |    'd e' f
        |  }
        |}""".stripMargin)
    linesEqual(marshaller.valueToPretty(strings),
      """[
        |  a
        |  'b c'
        |  'd e'
        |  f
        |  g
        |]""".stripMargin)
  }

  test("from value") {
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val rectangle = Rectangle(topLeft, bottomRight)
    val devonMarshaller = DevonMarshallerWiring.Default
    val devon = devonMarshaller.fromValue(rectangle)
    val compact = devonMarshaller.toCompact(devon)
    assert(compact === "{topLeft{x 1 y 2}bottomRight{x 3 y 4}}")
  }

  test("to value") {
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val expectedRectangle = Rectangle(topLeft, bottomRight)
    val compact = "{topLeft{x 1 y 2}bottomRight{x 3 y 4}}"
    val devonMarshaller = DevonMarshallerWiring.Default
    val devon = devonMarshaller.fromString(compact)
    val actualRectangle = devonMarshaller.toValue(devon, classOf[Rectangle])
    assert(actualRectangle === expectedRectangle)
  }

  test("composite from value") {
    val composite = SampleWithCompositeTypes(Map(1 -> "a"), Seq(Point(2, 3)))
    val devonMarshaller = DevonMarshallerWiring.Default
    val devon = devonMarshaller.fromValue(composite)
    val compact = devonMarshaller.toCompact(devon)
    assert(compact === "{sampleMap{1 a}sampleSeq[{x 2 y 3}]}")
  }

  test("composite to value") {
    val expected = SampleWithCompositeTypes(Map(1 -> "a"), Seq(Point(2, 3)))
    val compact = "{sampleMap{1 a}sampleSeq[{x 2 y 3}]}"
    val devonMarshaller = DevonMarshallerWiring.Default
    val devon = devonMarshaller.fromString(compact)
    val actual = devonMarshaller.toValue(devon, classOf[SampleWithCompositeTypes])
    assert(actual === expected)
  }

  test("preserve order") {
    val devonMarshaller = DevonMarshallerWiring.Default
    val value = PreserveOrder(1, 2, 3, 4, 5)
    val devon = devonMarshaller.fromValue(value)
    val compact = devonMarshaller.toCompact(devon)
    assert(compact === "{a 1 b 2 c 3 d 4 e 5}")
  }

  test("unescape when reading") {
    val devonWithEscapedNewline = """aa\nbb"""
    val expected = DevonString("aa\nbb")
    val actual = escapingDevonMarshaller.fromString(devonWithEscapedNewline)
    assert(actual === expected)
  }

  test("escape when writing compact") {
    val devonWithEscapedNewline = DevonString("aa\nbb")
    val expected = """aa\nbb"""
    val actual = escapingDevonMarshaller.toCompact(devonWithEscapedNewline)
    assert(actual === expected)
  }

  test("escape when writing pretty") {
    val devonWithEscapedNewline = DevonString("aa\nbb")
    val expected = Seq( """aa\nbb""")
    val actual = escapingDevonMarshaller.toPretty(devonWithEscapedNewline)
    assert(actual === expected)
  }

  def linesEqual(actual: Seq[String], expectedAsOneString: String): Unit = {
    val expected = expectedAsOneString.split("\n").toSeq
    val result = SeqDifference.diff(actual, expected)
    assert(result.isSame, result.messageLines.mkString("\n"))
  }

  val escapingDevonMarshaller = new DevonMarshallerImpl(
    compactFormatter = new CompactDevonFormatterImpl(EscapeStringProcessor),
    prettyFormatter = new PrettyDevonFormatterImpl(EscapeStringProcessor),
    devonReflection = new DevonReflectionImpl(
      new ReflectionImpl(SimpleTypeConversion.defaultConversions)),
    iteratorFactory = new DevonIteratorFactoryImpl(
      ruleLookup = new DevonRuleLookup,
      assembler = new DevonAssembler,
      tokenMarshaller = new TokenMarshallerImpl(EscapeStringProcessor)))
}
