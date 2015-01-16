package com.seanshubin.devon.prototype

import com.seanshubin.devon.core.devon._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.FunSuite
import org.scalatest.prop.PropertyChecks

class LearnPropertyBasedTestingTest extends FunSuite with PropertyChecks {
  test("length of two concatenated strings") {
    forAll { (left: String, right: String) =>
      whenever(left != null && right != null) {
        assert((left + right).length === left.length + right.length)
      }
    }
  }

  test("can't both win and lose") {
    val rpsGenerator: Gen[Rps] = Gen.oneOf(Rock, Paper, Scissors)
    implicit val arbitraryRps: Arbitrary[Rps] = Arbitrary(rpsGenerator)
    forAll { (left: Rps, right: Rps) =>
      whenever(left.beats(right)) {
        assert(!right.beats(left))
      }
    }
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

  test("converting between devon and compact preserves meaning") {
    implicit val arbitraryDevon = Arbitrary[Devon](genDevon)
    forAll { (devon1: Devon) =>
      val compact1 = Formatter.compactDevon(devon1).text
      val devon2 = DevonIterator.fromString(compact1).next()
      val compact2 = Formatter.compactDevon(devon2).text
      assert(compact1 === compact2)
    }
  }
}
