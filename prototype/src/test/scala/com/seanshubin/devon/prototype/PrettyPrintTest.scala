package com.seanshubin.devon.prototype

import com.seanshubin.devon.core.devon._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.FunSuite
import org.scalatest.prop.PropertyChecks

class PrettyPrintTest extends FunSuite with PropertyChecks {
  test("map") {
    val source = "{a b c d}"
    val devon = DevonIterator.parse(source)
    val actual = PrettyFormatter.pretty(devon)
    val expected =
      """{
        |  a b
        |  c d
        |}""".stripMargin
    assert(actual === expected)
  }

  test("empty string") {
    val source = "''"
    val actual = CompactFormatter.compactString(source)
    val expected = "''"
    assert(actual === expected)

  }
  test("complex") {
    val complex = "{a{b c}[d[e f]](){()[f{g h}()]{}'i j'}'k '' l'}"
    val devon = DevonIterator.parse(complex)
    val expected =
      """{
        |  a
        |  {
        |    b c
        |  }
        |  [
        |    d
        |    [
        |      e
        |      f
        |    ]
        |  ]
        |  ()
        |  {
        |    ()
        |    [
        |      f
        |      {
        |        g h
        |      }
        |      ()
        |    ]
        |    {} 'i j'
        |  }
        |  'k '' l'
        |}""".stripMargin
    val actual = PrettyFormatter.pretty(devon)
    assert(actual === expected)
  }

  def seqToMap(seq: Seq[Devon]): DevonMap = {
    val innerMap = seq.grouped(2).map {
      case Seq(a, b) => (a, b)
    }.toMap
    DevonMap(innerMap)
  }

  lazy val genChar: Gen[Char] = Gen.choose(32.toChar, 126.toChar)
  lazy val genSimpleString: Gen[String] = Gen.listOf(genChar).map(_.mkString)
  lazy val genString: Gen[Devon] = for {s <- genSimpleString} yield DevonString(s)
  lazy val genNull: Gen[Devon] = Gen.const(DevonNull)
  lazy val genArray: Gen[Devon] = Gen.choose(0, 10) flatMap { size => Gen.listOfN(size, genDevon)} flatMap (list => DevonArray(list))
  lazy val genMap: Gen[Devon] = Gen.choose(0, 5) flatMap { size => Gen.listOfN(size * 2, genDevon)} flatMap (list => seqToMap(list))
  lazy val genDevon: Gen[Devon] = Gen.frequency(
    (7, genString),
    (1, genMap),
    (1, genArray),
    (1, genNull))

  test("converting between devon and pretty preserves meaning") {
    implicit val arbitraryDevon = Arbitrary[Devon](genDevon)
    forAll { (devon1: Devon) =>
      val pretty1 = PrettyFormatter.pretty(devon1)
      val devon2 = DevonIterator.fromString(pretty1).next()
      val pretty2 = PrettyFormatter.pretty(devon2)
      assert(pretty1 === pretty2)
    }
  }
}
