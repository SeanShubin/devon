package com.seanshubin.devon.core.devon

import org.scalatest.FunSuite

class DevonTest extends FunSuite {
  test("simple merge") {
    val left =
      """{
        |  read b
        |  update c
        |  delete d
        |}
      """.stripMargin
    val right =
      """{
        |  create a
        |  update d
        |  delete ()
        |}
      """.stripMargin
    val expected = "{read b create a update d delete()}"
    val devonMarshaller = DevonMarshallerWiring.Default
    val leftDevon = devonMarshaller.fromString(left)
    val rightDevon = devonMarshaller.fromString(right)
    val mergedDevon = DevonUtil.merge(leftDevon, rightDevon)
    val actual = devonMarshaller.toCompact(mergedDevon)
    assert(actual === expected)
  }
}
