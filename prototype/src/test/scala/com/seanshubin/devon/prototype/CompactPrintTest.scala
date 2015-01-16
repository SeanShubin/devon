package com.seanshubin.devon.prototype

import org.scalatest.FunSuite

class CompactPrintTest extends FunSuite {
  test("map") {
    val source =
      """{
        |  a b
        |  c d
        |}""".stripMargin
    val actual = Formatter.compactString(source)
    val expected = "{a b c d}"
    assert(actual === expected)
  }

  test("empty string") {
    val source = "''"
    val actual = Formatter.compactString(source)
    val expected = "''"
    assert(actual === expected)

  }

  test("complex") {
    val complex =
      """{
        |  a {b c}
        |  [
        |    d
        |    [e f]
        |  ]
        |  ()
        |  {
        |    ()
        |    [
        |      f
        |      {g h}
        |      ()
        |    ]
        |    {} i
        |  }
        |  j
        |}
        |[
        |  [k l]
        |  []
        |  m
        |]
        |n
        |()
        |'o p'
        |'q '' r'""".stripMargin
    val actual = Formatter.compactString(complex)
    val expected = "{a{b c}[d[e f]](){()[f{g h}()]{}i}j}[[k l][]m]n()'o p' 'q '' r'"
    assert(actual === expected)
  }
}
