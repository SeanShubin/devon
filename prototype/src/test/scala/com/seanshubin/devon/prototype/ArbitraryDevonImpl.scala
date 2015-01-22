package com.seanshubin.devon.prototype

import com.seanshubin.devon.core.devon._

class ArbitraryDevonImpl(random: Random, maxDepth: Int, maxStringSize: Int, maxArraySize: Int, maxMapSize: Int) extends ArbitraryDevon {
  override def generate(): Devon = {
    generateDevon(maxDepth)
  }

  def generateDevon(depth: Int): Devon = {
    if (depth < 0) {
      random.nextInt(10) match {
        case 0 => DevonNull
        case _ => generateDevonString()
      }
    } else {
      random.nextInt(2) match {
        case 0 => generateArray(depth)
        case 1 => generateMap(depth)
      }
    }
  }

  def generateDevonString(): DevonString = {
    DevonString(generateString())
  }

  def generateString(): String = {
    val size = random.nextInt(maxStringSize + 1)
    generateStringOfSize(size)
  }

  def generateStringOfSize(size: Int): String = {
    val chars = for {index <- 0 until size} yield generateCharacter()
    chars.mkString
  }

  def generateCharacter(): Char = {
    val charCode = random.nextInt(127 - 32) + 32
    charCode.toChar
  }

  def generateArray(depth: Int): DevonArray = {
    val size = random.nextInt(maxArraySize + 1)
    val elements = for {index <- 0 until size} yield generateDevon(depth - 1)
    DevonArray(elements)
  }

  def generateMap(depth: Int): DevonMap = {
    val size = random.nextInt(maxMapSize + 1)
    val elements = for {index <- 0 until size * 2} yield generateDevon(depth - 1)
    val entries = elements.grouped(2).map(toTuple2)
    DevonMap(entries.toMap)
  }

  def toTuple2[T](seq: Seq[T]): (T, T) = {
    seq match {
      case Seq(key, value) => (key, value)
      case _ => throw new RuntimeException(s"Unable to convert $seq to a tuple 2")
    }
  }
}
