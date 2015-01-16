package com.seanshubin.devon.prototype

import com.seanshubin.devon.core.devon._
import com.seanshubin.devon.core.token.TokenCharacters
import com.seanshubin.devon.prototype.CompactFragment._

object Formatter {
  def compactString(source: String): String = {
    val devonIterator = DevonIterator.fromString(source)
    val devonSeq = devonIterator.toIndexedSeq
    compactDevon(DevonArray(devonIterator.toSeq)).text
    if (devonSeq.isEmpty) ""
    else devonSeq.map(compactDevon).reduceLeft(CompactFragment.merge).text
  }

  def compactDevon(devon: Devon): CompactFragment = {
    devon match {
      case DevonNull => CompactFragment(Symbol, "()", Symbol)
      case DevonString(string) => compactDevonString(string)
      case DevonArray(array) => compactArray(array)
      case DevonMap(map) => compactMap(map)
      case x => throw new RuntimeException(s"Unable to format $x")
    }
  }

  def compactDevonString(string: String): CompactFragment = {
    if (string.exists(x => TokenCharacters.structuralAndWhitespace.contains(x)) || string.isEmpty) {
      compactQuotedString(string)
    } else {
      compactUnquotedString(string)
    }
  }

  def compactQuotedString(string: String): CompactFragment = {
    val escaped = string.replaceAll("\'", "\'\'")
    val wrapped = "\'" + escaped + "\'"
    CompactFragment(Quoted, wrapped, Quoted)
  }

  def compactUnquotedString(string: String): CompactFragment = CompactFragment(Unquoted, string, Unquoted)

  def compactArray(array: Seq[Devon]): CompactFragment = {
    if (array.isEmpty) CompactFragment(Symbol, "[]", Symbol)
    else {
      val text = "[" + array.map(compactDevon).reduceLeft(CompactFragment.merge).text + "]"
      CompactFragment(Symbol, text, Symbol)
    }
  }

  def compactMap(map: Map[Devon, Devon]): CompactFragment = {
    def tuple2ToSeq(tuple2: (Devon, Devon)): Seq[Devon] = {
      val (first, second) = tuple2
      Seq(first, second)
    }
    val entries = map.flatMap(tuple2ToSeq)
    if (entries.isEmpty) CompactFragment(Symbol, "{}", Symbol)
    else {
      CompactFragment(
        Symbol,
        "{" + entries.map(compactDevon).reduceLeft(CompactFragment.merge).text + "}",
        Symbol)
    }
  }
}
