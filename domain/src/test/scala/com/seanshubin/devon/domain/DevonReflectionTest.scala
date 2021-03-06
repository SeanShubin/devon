package com.seanshubin.devon.domain

import java.time.{LocalDate, LocalTime, ZoneId, ZonedDateTime}

import org.scalatest.FunSuite

import scala.collection.immutable.ListMap
import scala.reflect.runtime.universe

class DevonReflectionTest extends FunSuite {
  test("primitive types") {
    val value = SampleWithPrimitiveTypes(1, 2, 'a', 3, 4, 5.6F, 7.8, sampleBoolean = true)
    val devon = DevonMap(Map(
      DevonString("sampleByte") -> DevonString("1"),
      DevonString("sampleShort") -> DevonString("2"),
      DevonString("sampleChar") -> DevonString("a"),
      DevonString("sampleInt") -> DevonString("3"),
      DevonString("sampleLong") -> DevonString("4"),
      DevonString("sampleFloat") -> DevonString("5.6"),
      DevonString("sampleDouble") -> DevonString("7.8"),
      DevonString("sampleBoolean") -> DevonString("true")))
    testReflection(value, devon, classOf[SampleWithPrimitiveTypes])
  }

  test("top level types") {
    val sampleDate = LocalDate.of(2015, 2, 25)
    val sampleTime = LocalTime.of(15, 1, 23)
    val sampleZoneId = ZoneId.of("America/Los_Angeles")
    val sampleZonedDateTime = ZonedDateTime.of(sampleDate, sampleTime, sampleZoneId)
    val value = SampleWithTopLevelTypes(
      sampleString = "Hello",
      sampleBigInt = BigInt("123"),
      sampleBigDecimal = BigDecimal("12.34"),
      sampleZonedDateTime = sampleZonedDateTime,
      sampleInstant = sampleZonedDateTime.toInstant)
    val devon = DevonMap(Map(
      DevonString("sampleString") -> DevonString("Hello"),
      DevonString("sampleBigInt") -> DevonString("123"),
      DevonString("sampleBigDecimal") -> DevonString("12.34"),
      DevonString("sampleZonedDateTime") -> DevonString("2015-02-25T15:01:23-08:00[America/Los_Angeles]"),
      DevonString("sampleInstant") -> DevonString("2015-02-25T23:01:23Z")))
    testReflection(value, devon, classOf[SampleWithTopLevelTypes])
  }

  test("composite types") {
    val value = SampleWithCompositeTypes(Map(1 -> "a"), Seq(Point(2, 3)))
    val devon = DevonMap(Map(
      DevonString("sampleMap") -> DevonMap(Map(
        DevonString("1") -> DevonString("a"))),
      DevonString("sampleSeq") -> DevonArray(Seq(
        DevonMap(Map(
          DevonString("x") -> DevonString("2"),
          DevonString("y") -> DevonString("3")))))))
    testReflection(value, devon, classOf[SampleWithCompositeTypes])
  }

  test("sequence") {
    val value = Seq(1, 2, 3)
    val devon = DevonArray(Seq(DevonString("1"), DevonString("2"), DevonString("3")))
    testReflection(value, devon, classOf[Seq[Int]])
  }

  test("list") {
    val value = List(1, 2, 3)
    val devon = DevonArray(Seq(DevonString("1"), DevonString("2"), DevonString("3")))
    testReflection(value, devon, classOf[List[Int]])
  }

  test("map") {
    val value = Map(1 -> "a", 2 -> "b", 3 -> "c", 4 -> "d", 5 -> "e")
    val devon = DevonMap(Map(
      DevonString("1") -> DevonString("a"),
      DevonString("2") -> DevonString("b"),
      DevonString("3") -> DevonString("c"),
      DevonString("4") -> DevonString("d"),
      DevonString("5") -> DevonString("e")))
    testReflection(value, devon, classOf[Map[Int, String]])
  }

  test("list map") {
    val value = ListMap(1 -> "a", 2 -> "b", 3 -> "c", 4 -> "d", 5 -> "e")
    val devon = DevonMap(Map(
      DevonString("1") -> DevonString("a"),
      DevonString("2") -> DevonString("b"),
      DevonString("3") -> DevonString("c"),
      DevonString("4") -> DevonString("d"),
      DevonString("5") -> DevonString("e")))
    testReflection(value, devon, classOf[ListMap[Int, String]])
  }

  test("map preserves order") {
    val original = ListMap(1 -> "a", 2 -> "b", 3 -> "c", 4 -> "d", 5 -> "e")
    val devonReflection = DevonMarshallerWiring.builder().buildWiring().devonReflection
    val devon = devonReflection.fromValue(original)
    val actual = devonReflection.toValue(devon, classOf[ListMap[Int, String]])
    assert(actual.keys === original.keys)
  }

  def testReflection[T: universe.TypeTag](value: T, devon: Devon, theClass: Class[T]): Unit = {
    val devonReflection = DevonMarshallerWiring.builder().buildWiring().devonReflection
    val actualDevon: Devon = devonReflection.fromValue(value)
    assert(actualDevon === devon)
    val actualValue: T = devonReflection.toValue(devon, theClass)
    assert(actualValue === value)
  }
}
