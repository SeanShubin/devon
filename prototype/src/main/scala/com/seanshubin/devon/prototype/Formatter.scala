package com.seanshubin.devon.prototype

import com.seanshubin.devon.core.devon._
import com.seanshubin.devon.core.token.TokenCharacters

object Formatter {
  def compactString(source:String): String = {
    val devonIterator = DevonIterator.fromString(source)
    devonIterator.map(compactDevon).mkString
  }

  def compactDevon(devon:Devon):String = {
    devon match {
      case DevonNull => "()"
      case DevonString(string) => compactDevonString(string)
      case DevonArray(array) => compactArray(array)
      case DevonMap(map) => compactMap(map)
      case x => throw new RuntimeException(s"Unable to format $x")
    }
  }

  def compactDevonString(string:String):String = {
    if(string.exists(x => TokenCharacters.structuralAndWhitespace.contains(x))) {
      compactQuotedString(string)
    } else {
      compactUnquotedString(string)
    }
  }

  def compactQuotedString(string:String):String = {
    val escaped = string.replaceAll("\'", "\'\'")
    val wrapped = "\'" + escaped + "\'"
    wrapped
  }

  def compactUnquotedString(string:String):String = string

  def compactArray(array:Seq[Devon]):String = {
    "[" + array.map(compactDevon).mkString(" ") + "]"
  }

  def compactMap(map:Map[Devon, Devon]):String = {
    def tuple2ToSeq(tuple2:(Devon, Devon)):Seq[Devon] = {
      val (first, second) = tuple2
      Seq(first, second)
    }
    val entries = map.flatMap(tuple2ToSeq)
    "{" + entries.map(compactDevon).mkString(" ") + "}"
  }
}
