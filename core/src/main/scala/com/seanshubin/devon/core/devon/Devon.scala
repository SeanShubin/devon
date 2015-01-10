package com.seanshubin.devon.core.devon

trait Devon

case object DevonNull extends Devon

case class DevonString(string: String) extends Devon

case class DevonArray(array: Seq[Devon]) extends Devon

case class DevonMap(map: Map[Devon, Devon]) extends Devon
