package com.seanshubin.devon.prototype

class RandomImpl(delegate: scala.util.Random) extends Random {
  override def nextInt(choices: Int): Int = delegate.nextInt(choices)
}
