package com.seanshubin.devon.prototype

import org.scalacheck.{Gen, Arbitrary}
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
    val rpsGenerator:Gen[Rps] = Gen.oneOf(Rock, Paper, Scissors)
    implicit val arbitraryRps:Arbitrary[Rps] = Arbitrary(rpsGenerator)
    forAll { (left: Rps, right: Rps) =>
      whenever(left.beats(right)) {
        assert(!right.beats(left))
      }
    }
  }
}
