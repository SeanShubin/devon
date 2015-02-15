package com.seanshubin.devon.prototype

import com.seanshubin.devon.core.devon._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.FunSuite
import org.scalatest.prop.PropertyChecks

class PrettyPrintTest extends FunSuite with PropertyChecks {
  val devonMarshaller = new DevonMarshallerImpl

  def stringToDevon(source: String): Devon = {
    val devon = devonMarshaller.fromString(source)
    devon
  }

  def prettyString(source: String): Seq[String] = {
    devonToPretty(stringToDevon(source))
  }

  def devonToPretty(devon: Devon): Seq[String] = {
    val pretty = devonMarshaller.toPretty(devon)
    pretty
  }

  ignore("map") {
    val source = "{a b c d}"
    val devon = stringToDevon(source)
    val actual = devonToPretty(devon).mkString("\n")
    val expected =
      """{
        |  a b
        |  c d
        |}""".stripMargin
    assert(actual === expected)
  }

  test("empty string") {
    val source = "''"
    val actual = devonMarshaller.toCompact(devonMarshaller.fromString(source))
    val expected = "''"
    assert(actual === expected)

  }
  ignore("complex") {
    val complex = "{a{b c}[d[e f]](){()[f{g h}()]{}'i j'}'k '' l'}"
    val devon = stringToDevon(complex)
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
    val actual = devonToPretty(devon).mkString("\n")
    assert(actual === expected)
  }

  ignore("rectangle") {
    val text = "{ topLeft { x 1 y 2 } bottomRight { x 3 y 4 } }"
    val devon = stringToDevon(text)
    val actual = devonToPretty(devon).mkString("\n")
    val expected =
      """{
        |  topLeft
        |  {
        |    x 1
        |    y 2
        |  }
        |  bottomRight
        |  {
        |    x 3
        |    y 4
        |  }
        |}""".stripMargin
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
      val pretty1 = devonToPretty(devon1)
      val devon2 = stringToDevon(pretty1.mkString("\n"))
      val pretty2 = devonToPretty(devon2)
      assert(pretty1 === pretty2)
    }
  }
}
