package com.seanshubin.devon.core.devon

sealed trait Devon

case object DevonNull extends Devon

case class DevonString(string: String) extends Devon

case class DevonArray(array: Seq[Devon]) extends Devon

case class DevonMap(map: Map[Devon, Devon]) extends Devon

object Devon {
  def merge(left: Devon, right: Devon): Devon = {
    (left, right) match {
      case (x: DevonMap, y: DevonMap) =>
        DevonMap(y.map.foldLeft(x.map)(mergeEntry))
      case (x, y) => y
    }
  }

  private def mergeEntry(aMap: Map[Devon, Devon], bEntry: (Devon, Devon)): Map[Devon, Devon] = {
    val (bKey, bValue) = bEntry
    val merged = if (bValue == null) {
      aMap - bKey
    } else {
      val cValue = aMap.get(bKey) match {
        case Some(aValue) => merge(aValue, bValue)
        case None => bValue
      }
      aMap.updated(bKey, cValue)
    }
    merged
  }
}
