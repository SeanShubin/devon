package com.seanshubin.devon.core

import org.scalatest.FunSuite

import scala.reflect.ClassTag
import scala.reflect.runtime.universe

class ReflectUtilTest extends FunSuite {
  test("create class") {
    val values = Map("x" -> "1", "y" -> "2")
    val created: Point = ReflectUtil.construct(values, classOf[Point])
    assert(created === Point(1, 2))
  }

  test("pull apart class") {
    val value = Point(1, 2)
    val map = ReflectUtil.pullApart(value)
    assert(map === Map("x" -> "1", "y" -> "2"))
  }

  test("create nested class") {
    val values = Map("topLeft" -> Map("x" -> "1", "y" -> "2"), "bottomRight" -> Map("x" -> "3", "y" -> "4"))
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val rectangle = Rectangle(topLeft, bottomRight)
    val created: Rectangle = ReflectUtil.construct(values, classOf[Rectangle])
    assert(created === rectangle)
  }

  test("pull apart nested class") {
    val values = Map("topLeft" -> Map("x" -> "1", "y" -> "2"), "bottomRight" -> Map("x" -> "3", "y" -> "4"))
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val rectangle = Rectangle(topLeft, bottomRight)
    val map = ReflectUtil.pullApart(rectangle)
    assert(map === values)
  }

  test("primitive types") {
    val composed = SampleWithPrimitiveTypes(
      sampleByte = 1,
      sampleShort = 2,
      sampleChar = 'a',
      sampleInt = 3,
      sampleLong = 4,
      sampleFloat = 5.6F,
      sampleDouble = 7.8,
      sampleBoolean = true
    )
    val parts = Map(
      "sampleByte" -> "1",
      "sampleShort" -> "2",
      "sampleChar" -> "a",
      "sampleInt" -> "3",
      "sampleLong" -> "4",
      "sampleFloat" -> "5.6",
      "sampleDouble" -> "7.8",
      "sampleBoolean" -> "true"
    )
    testMarshallBothDirections(composed, parts, classOf[SampleWithPrimitiveTypes])
  }

  test("top level types") {
    val composed = SampleWithTopLevelTypes(
      sampleString = "sampleString",
      sampleBigInt = BigInt("12345"),
      sampleBigDecimal = BigDecimal("123.456")
    )
    val parts = Map(
      "sampleString" -> "sampleString",
      "sampleBigInt" -> "12345",
      "sampleBigDecimal" -> "123.456"
    )
    testMarshallBothDirections(composed, parts, classOf[SampleWithTopLevelTypes])
  }

  test("map") {
    val composed = SampleWithMap(
      sampleMap = Map(1 -> "a", 2 -> "b")
    )
    val parts = Map(
      "sampleMap" -> Map("1" -> "a", "2" -> "b")
    )
    testMarshallBothDirections(composed, parts, classOf[SampleWithMap])
  }

  ignore("composite types") {
    val composed = SampleWithCompositeTypes(
      sampleMap = Map(1 -> "a", 2 -> "b"),
      sampleSeq = Seq(Point(1, 2), Point(3, 4))
    )
    val parts = Map(
      "sampleMap" -> Map("1" -> "a", "2" -> "b"),
      "sampleSeq" -> Seq(Map("x" -> "1", "y" -> "2"), Map("x" -> "3", "y" -> "4"))
    )
    testMarshallBothDirections(composed, parts, classOf[SampleWithCompositeTypes])
  }

  def testMarshallBothDirections[T: universe.TypeTag : ClassTag](composed: T, parts: Map[String, Any], theClass: Class[T]): Unit = {
    val actualComposed = ReflectUtil.construct(parts, theClass)
    assert(actualComposed === composed)
    val actualParts = ReflectUtil.pullApart(composed)
    assert(actualParts === parts)
  }

}
