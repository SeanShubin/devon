package com.seanshubin.devon.reflection

import java.io.File
import java.nio.file.{Path, Paths}
import java.time.{Instant, ZonedDateTime}

import org.scalatest.FunSuite

import scala.collection.immutable.ListMap
import scala.reflect.runtime._

class ReflectionTest extends FunSuite {
  test("int") {
    val staticallyTyped: Int = 12345
    val dynamicallyTyped = "12345"
    testReflection(staticallyTyped, classOf[Int], dynamicallyTyped)
  }

  test("short") {
    val staticallyTyped: Short = 12345
    val dynamicallyTyped = "12345"
    testReflection(staticallyTyped, classOf[Short], dynamicallyTyped)
  }

  test("byte") {
    val staticallyTyped: Byte = 123
    val dynamicallyTyped = "123"
    testReflection(staticallyTyped, classOf[Byte], dynamicallyTyped)
  }

  test("char") {
    val staticallyTyped: Char = 'a'
    val dynamicallyTyped = "a"
    testReflection(staticallyTyped, classOf[Char], dynamicallyTyped)
  }

  test("long") {
    val staticallyTyped: Long = 12345L
    val dynamicallyTyped = "12345"
    testReflection(staticallyTyped, classOf[Long], dynamicallyTyped)
  }

  test("float") {
    val staticallyTyped: Float = 12.34F
    val dynamicallyTyped = "12.34"
    testReflection(staticallyTyped, classOf[Float], dynamicallyTyped)
  }

  test("double") {
    val staticallyTyped: Double = 12.34
    val dynamicallyTyped = "12.34"
    testReflection(staticallyTyped, classOf[Double], dynamicallyTyped)
  }

  test("boolean") {
    val staticallyTyped: Boolean = true
    val dynamicallyTyped = "true"
    testReflection(staticallyTyped, classOf[Boolean], dynamicallyTyped)
  }

  test("null") {
    val staticallyTyped: Null = null
    val dynamicallyTyped = null
    testReflection(staticallyTyped, classOf[Null], dynamicallyTyped)
  }

  test("unit") {
    val staticallyTyped: Unit = ()
    val dynamicallyTyped = "()"
    testReflection(staticallyTyped, classOf[Unit], dynamicallyTyped)
  }

  test("string") {
    val staticallyTyped: String = "abcde"
    val dynamicallyTyped = "abcde"
    testReflection(staticallyTyped, classOf[String], dynamicallyTyped)
  }

  test("big int") {
    val staticallyTyped: BigInt = BigInt("12345")
    val dynamicallyTyped = "12345"
    testReflection(staticallyTyped, classOf[BigInt], dynamicallyTyped)
  }

  test("big decimal") {
    val staticallyTyped: BigDecimal = BigDecimal("12.34")
    val dynamicallyTyped = "12.34"
    testReflection(staticallyTyped, classOf[BigDecimal], dynamicallyTyped)
  }

  test("zoned date time") {
    val staticallyTyped: ZonedDateTime = ZonedDateTime.parse("2015-02-24T09:40:03.370-08:00[America/Los_Angeles]")
    val dynamicallyTyped = "2015-02-24T09:40:03.370-08:00[America/Los_Angeles]"
    testReflection(staticallyTyped, classOf[ZonedDateTime], dynamicallyTyped)
  }

  test("instant") {
    val staticallyTyped: Instant = Instant.parse("2015-02-24T17:45:57.612Z")
    val dynamicallyTyped = "2015-02-24T17:45:57.612Z"
    testReflection(staticallyTyped, classOf[Instant], dynamicallyTyped)
  }

  test("path") {
    val staticallyTyped: Path = Paths.get("foo", "bar")
    val dynamicallyTyped = staticallyTyped.toString
    testReflection(staticallyTyped, classOf[Path], dynamicallyTyped)
  }

  test("file") {
    val staticallyTyped: File = new File("foo-bar.txt")
    val dynamicallyTyped = staticallyTyped.toString
    testReflection(staticallyTyped, classOf[File], dynamicallyTyped)
  }

  test("case class") {
    val staticallyTyped: Point = Point(1, 2)
    val dynamicallyTyped = Map("x" -> "1", "y" -> "2")
    testReflection(staticallyTyped, classOf[Point], dynamicallyTyped)
  }

  test("tuple 2") {
    val staticallyTyped: (Int, String) = (1, "a")
    val dynamicallyTyped = Seq("1", "a")
    testReflection(staticallyTyped, classOf[(Int, String)], dynamicallyTyped)
  }

  test("tuple 3") {
    val staticallyTyped: (Int, String, Double) = (1, "a", 2.3)
    val dynamicallyTyped = Seq("1", "a", "2.3")
    testReflection(staticallyTyped, classOf[(Int, String, Double)], dynamicallyTyped)
  }

  test("nested case class") {
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val staticallyTyped: Rectangle = Rectangle(topLeft, bottomRight)
    val dynamicallyTyped = Map("topLeft" -> Map("x" -> "1", "y" -> "2"), "bottomRight" -> Map("x" -> "3", "y" -> "4"))
    testReflection(staticallyTyped, classOf[Rectangle], dynamicallyTyped)
  }

  test("sequence") {
    val staticallyTyped: Seq[Int] = Seq(1, 2, 3)
    val dynamicallyTyped = Seq("1", "2", "3")
    testReflection(staticallyTyped, classOf[Seq[Int]], dynamicallyTyped)
  }

  test("list") {
    val staticallyTyped: List[Int] = List(1, 2, 3)
    val dynamicallyTyped = List("1", "2", "3")
    testReflection(staticallyTyped, classOf[List[Int]], dynamicallyTyped)
  }

  test("map") {
    val staticallyTyped: Map[Int, String] = Map(1 -> "a", 2 -> "b", 3 -> "c")
    val dynamicallyTyped = Map("1" -> "a", "2" -> "b", "3" -> "c")
    testReflection(staticallyTyped, classOf[Map[Int, String]], dynamicallyTyped)
  }

  test("has option some") {
    val staticallyTyped: HasOption = HasOption(Some("a"))
    val dynamicallyTyped = Map("maybeValue" -> "a")
    testReflection(staticallyTyped, classOf[HasOption], dynamicallyTyped)
  }

  test("has option null") {
    val staticallyTyped: HasOption = HasOption(None)
    val dynamicallyTyped = Map("maybeValue" -> null)
    testReflection(staticallyTyped, classOf[HasOption], dynamicallyTyped)
  }

  test("has option none") {
    val staticallyTyped: HasOption = HasOption(None)
    val dynamicallyTyped = Map()
    val reflection = new ReflectionImpl(SimpleTypeConversion.defaultConversions)
    val piecedTogether = reflection.pieceTogether(dynamicallyTyped, classOf[HasOption])
    assert(piecedTogether === staticallyTyped)
  }

  test("case classes preserve order") {
    val sample = PreserveOrder(1, 2, 3, 4, 5)
    val reflection = new ReflectionImpl(SimpleTypeConversion.defaultConversions)
    val pulledApart = reflection.pullApart(sample)
    val pulledApartInOrder = pulledApart.asInstanceOf[Map[String, Int]].toSeq
    val expected = Seq("a" -> "1", "b" -> "2", "c" -> "3", "d" -> "4", "e" -> "5")
    assert(pulledApartInOrder === expected)
  }

  test("list maps preserve order on output") {
    val sample = ListMap("a" -> "1", "b" -> "2", "c" -> "3", "d" -> "4", "e" -> "5")
    val reflection = new ReflectionImpl(SimpleTypeConversion.defaultConversions)
    val pulledApart = reflection.pullApart(sample)
    val pulledApartInOrder = pulledApart.asInstanceOf[Map[String, Int]].toSeq
    val expected = Seq("a" -> "1", "b" -> "2", "c" -> "3", "d" -> "4", "e" -> "5")
    assert(pulledApartInOrder === expected)
  }

  test("list maps preserve order on input") {
    val dynamicallyTyped = ListMap("a" -> "1", "b" -> "2", "c" -> "3", "d" -> "4", "e" -> "5")
    val reflection = new ReflectionImpl(SimpleTypeConversion.defaultConversions)
    val piecedTogether = reflection.pieceTogether(dynamicallyTyped, classOf[ListMap[String, String]])
    val piecedTogetherInOrder = piecedTogether.toSeq
    val expected = Seq("a" -> "1", "b" -> "2", "c" -> "3", "d" -> "4", "e" -> "5")
    assert(piecedTogetherInOrder === expected)
  }

  test("support sets") {
    val staticallyTyped: HasSet = HasSet(Set("a"))
    val dynamicallyTyped = Map("values" -> Seq("a"))
    testReflection(staticallyTyped, classOf[HasSet], dynamicallyTyped)
  }

  test("sensible error when missing a required primitive") {
    val dynamicallyTyped = Map("x" -> "1")
    val reflection = new ReflectionImpl(SimpleTypeConversion.defaultConversions)
    val exception = intercept[RuntimeException] {
      reflection.pieceTogether(dynamicallyTyped, classOf[Point])
    }
    assert(exception.getMessage === "Missing value for y of type Int")
  }

  test("nested array") {
    val staticallyTyped: Seq[Seq[Int]] = Seq(Seq(1, 2), Seq(3, 4))
    val dynamicallyTyped = Seq(Seq("1", "2"), Seq("3", "4"))
    testReflection(staticallyTyped, classOf[Seq[Seq[Int]]], dynamicallyTyped)
  }

  test("seq of case class") {
    val staticallyTyped: Seq[Point] = Seq(Point(1, 2), Point(3, 4))
    val dynamicallyTyped = Seq(Map("x" -> "1", "y" -> "2"), Map("x" -> "3", "y" -> "4"))
    testReflection(staticallyTyped, classOf[Seq[Point]], dynamicallyTyped)
  }

  test("null seq") {
    val staticallyTyped: Seq[Point] = null
    val dynamicallyTyped = null
    testReflection(staticallyTyped, classOf[Seq[Point]], dynamicallyTyped)
  }

  test("null set") {
    val staticallyTyped: Set[Point] = null
    val dynamicallyTyped = null
    testReflection(staticallyTyped, classOf[Set[Point]], dynamicallyTyped)
  }

  test("null map") {
    val staticallyTyped: Map[Int, Int] = null
    val dynamicallyTyped = null
    testReflection(staticallyTyped, classOf[Map[Int, Int]], dynamicallyTyped)
  }

  test("null case class") {
    val staticallyTyped: Point = null
    val dynamicallyTyped = null
    testReflection(staticallyTyped, classOf[Point], dynamicallyTyped)
  }

  case class GroupArtifactVersion(group: String, artifact: String, version: String)

  case class GroupAndArtifact(group: String, artifact: String) extends Ordered[GroupAndArtifact] {
    def urlPath: String = "/" + dotToSlash(group) + "/" + artifact

    private def dotToSlash(s: String): String = s.replaceAll("\\.", "/")

    override def compare(that: GroupAndArtifact): Int = {
      Ordering.Tuple2(Ordering.String, Ordering.String).compare((group, artifact), (that.group, that.artifact))
    }
  }

  case class Configuration(pomFileName: String,
                           directoriesToSearch: Seq[Path],
                           directoryNamesToSkip: Set[String],
                           mavenRepositories: Seq[String],
                           doNotUpgradeFrom: Set[GroupAndArtifact],
                           doNotUpgradeTo: Set[GroupArtifactVersion],
                           automaticallyUpgrade: Boolean,
                           reportDirectory: Path,
                           cacheDirectory: Path,
                           cacheExpire: String)

  def testReflection[T: universe.TypeTag](staticallyTyped: T, theClass: Class[T], dynamicallyTyped: Any) = {
    val reflection = new ReflectionImpl(SimpleTypeConversion.defaultConversions)
    val piecedTogether = reflection.pieceTogether(dynamicallyTyped, theClass)
    assert(piecedTogether === staticallyTyped)
    val pulledApart = reflection.pullApart(staticallyTyped)
    assert(pulledApart === dynamicallyTyped)
  }
}
