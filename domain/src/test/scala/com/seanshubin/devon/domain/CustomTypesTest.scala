package com.seanshubin.devon.domain

import com.seanshubin.devon.parserules.DevonMarshaller
import org.scalatest.FunSuite

class CustomTypesTest extends FunSuite {
  val marshaller: DevonMarshaller = DevonMarshallerWiring.builder().addConversion(ColorConversion).build()

  test("marshall custom type to devon") {
    val rgbValues: Map[Color, Int] = Map(Color.Red -> 153, Color.Green -> 50, Color.Blue -> 204)
    val devon = marshaller.fromValue(rgbValues)
    val compact = marshaller.toCompact(devon)
    assert(compact === "{red 153 green 50 blue 204}")
  }

  test("marshall custom type from devon") {
    val text = "{red 153 green 50 blue 204}"
    val expected: Map[Color, Int] = Map(Color.Red -> 153, Color.Green -> 50, Color.Blue -> 204)
    val actual = marshaller.stringToValue(text, classOf[Map[Color, Int]])
    assert(actual === expected)
  }
}
