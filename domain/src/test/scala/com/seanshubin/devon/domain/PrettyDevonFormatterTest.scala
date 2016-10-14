package com.seanshubin.devon.domain

import org.scalatest.FunSuite

class PrettyDevonFormatterTest extends FunSuite {
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

  def linesEqual(actual: Seq[String], expectedAsOneString: String): Unit = {
    val expected = expectedAsOneString.split("\n").toSeq
    val result = SeqDifference.diff(actual, expected)
    assert(result.isSame, result.messageLines.mkString("\n"))
  }
}
