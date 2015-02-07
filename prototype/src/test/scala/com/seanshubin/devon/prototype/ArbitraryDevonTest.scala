package com.seanshubin.devon.prototype

import org.scalatest.FunSuite

class ArbitraryDevonTest extends FunSuite {
  test("print big devon") {
    val seed = 12345L
    val scalaRandom = new scala.util.Random(seed)
    val random = new RandomImpl(scalaRandom)
    val generator = new ArbitraryDevonImpl(random = random, maxDepth = 5, maxStringSize = 5, maxArraySize = 10, maxMapSize = 5)
    val devon = generator.generate()
    val prettyDevon = PrettyFormatter.pretty(devon)
    assert(prettyDevon.size === 23268)
  }
}
