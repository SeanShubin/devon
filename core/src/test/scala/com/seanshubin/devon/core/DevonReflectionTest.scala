package com.seanshubin.devon.core

import java.time.{LocalDate, LocalTime, ZoneId, ZonedDateTime}

import com.seanshubin.devon.core.devon._
import org.scalatest.FunSuite

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

  def testReflection[T: universe.TypeTag](value: T, devon: Devon, theClass: Class[T]): Unit = {
    val devonReflection = new DefaultDevonReflection
    val actualDevon: Devon = devonReflection.fromValue(value)
    assert(actualDevon === devon)
    val actualValue: T = devonReflection.toValue(devon, theClass)
    assert(actualValue === value)
  }
}
