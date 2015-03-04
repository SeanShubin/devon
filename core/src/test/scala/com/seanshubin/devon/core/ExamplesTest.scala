package com.seanshubin.devon.core

import com.seanshubin.devon.core.devon.DefaultDevonMarshaller
import org.scalatest.FunSuite

class ExamplesTest extends FunSuite {

  case class GroupArtifact(group: String, artifact: String)

  test("composite key") {
    val value: Map[GroupArtifact, Seq[String]] = Map(
      GroupArtifact("org.joda", "joda-convert") -> Seq("1.7", "1.6", "1.5"),
      GroupArtifact("joda-time", "joda-time") -> Seq("2.7", "2.6", "2.5")
    )
    val expected =
      """{
        |  {
        |    group org.joda
        |    artifact joda-convert
        |  }
        |  [
        |    1.7
        |    1.6
        |    1.5
        |  ]
        |  {
        |    group joda-time
        |    artifact joda-time
        |  }
        |  [
        |    2.7
        |    2.6
        |    2.5
        |  ]
        |}""".stripMargin
    val actual = DefaultDevonMarshaller.valueToPretty(value).mkString("\n")
    assert(expected === actual)
  }

  test("urls") {
    val value: Seq[String] = Seq("http://example.com/document.txt#line=10,20",
      "http://example.com/foo.mp4#t=10,20",
      "http://example.com/bar.webm#t=40,80&xywh=160,120,320,240")
    val expected =
      """[
        |  http://example.com/document.txt#line=10,20
        |  http://example.com/foo.mp4#t=10,20
        |  http://example.com/bar.webm#t=40,80&xywh=160,120,320,240
        |]""".stripMargin
    val actual = DefaultDevonMarshaller.valueToPretty(value).mkString("\n")
    assert(expected === actual)
  }

  test("paths") {
    val value: Seq[String] = Seq(
      "C:\\Program Files", "C:\\Winnt", "C:\\Winnt\\System32")
    val expected =
      """[
        |  'C:\Program Files'
        |  C:\Winnt
        |  C:\Winnt\System32
        |]""".stripMargin
    val actual = DefaultDevonMarshaller.valueToPretty(value).mkString("\n")
    assert(expected === actual)
  }

  test("simple strings") {
    val value: Seq[String] = Seq(
      "Hello",
      "World",
      "",
      "Hello, world!",
      "Sean's favorite notation"
    )
    val expected =
      """[
        |  Hello
        |  World
        |  ''
        |  'Hello, world!'
        |  'Sean''s favorite notation'
        |]""".stripMargin
    val actual = DefaultDevonMarshaller.valueToPretty(value).mkString("\n")
    assert(expected === actual)
  }

  test("patch request") {
    val value: Map[String, String] = Map(
      "sku" -> "123",
      "price" -> "499.99",
      "seasonal discount" -> null
    )
    val expected =
      """{
        |  sku 123
        |  price 499.99
        |  'seasonal discount' ()
        |}""".stripMargin
    val actual = DefaultDevonMarshaller.valueToPretty(value).mkString("\n")
    assert(expected === actual)
  }
}
