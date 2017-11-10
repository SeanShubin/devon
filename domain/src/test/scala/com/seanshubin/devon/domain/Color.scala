package com.seanshubin.devon.domain

import scala.collection.mutable.ArrayBuffer

sealed abstract case class Color(name: String) {
  Color.valuesBuffer += this

  def matchesName(targetName: String): Boolean = name.equalsIgnoreCase(targetName)
}

object Color {
  private val valuesBuffer = new ArrayBuffer[Color]
  lazy val values = valuesBuffer.toSeq
  val Red = new Color("red") {}
  val Green = new Color("green") {}
  val Blue = new Color("blue") {}

  def fromString(name: String): Color = values.find(_.matchesName(name)).get
}
