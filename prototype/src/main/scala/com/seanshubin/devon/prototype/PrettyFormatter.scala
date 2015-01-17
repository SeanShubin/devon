package com.seanshubin.devon.prototype

import com.seanshubin.devon.core.devon._
import com.seanshubin.devon.core.token.TokenCharacters

object PrettyFormatter {
  def pretty(devon: Devon): String = {
    prettyDevon(devon).lines.mkString("\n")
  }

  def prettyDevon(devon: Devon): PrettyFragment = {
    devon match {
      case x: DevonString => prettyString(x)
      case x: DevonMap => prettyMap(x)
      case x: DevonArray => prettyArray(x)
      case DevonNull => prettyNull()
      case x => throw new RuntimeException(s"unable to pretty format: $x")
    }
  }

  def prettyString(devon: DevonString): PrettyFragment = {
    if (devon.string.exists(x => TokenCharacters.structuralAndWhitespace.contains(x)) || devon.string.isEmpty) {
      prettyQuotedString(devon)
    } else {
      prettyUnquotedString(devon)
    }
  }

  def prettyMap(devon: DevonMap): PrettyFragment = {
    if (devon.map.size == 0) PrettyFragment(Seq("{}"))
    else {
      val fragments: Seq[PrettyFragment] = devon.map.map(prettyEntry).toSeq
      val combined: PrettyFragment = fragments.reduceLeft(PrettyFragment.combine)
      PrettyFragment(Seq("{") ++ combined.lines.map(indent) ++ Seq("}"))
    }
  }

  def prettyArray(devon: DevonArray): PrettyFragment = {
    if (devon.array.size == 0) PrettyFragment(Seq("[]"))
    else {
      val fragments: Seq[PrettyFragment] = devon.array.map(prettyDevon).toSeq
      val combined: PrettyFragment = fragments.reduceLeft(PrettyFragment.combine)
      PrettyFragment(Seq("[") ++ combined.lines.map(indent) ++ Seq("]"))
    }
  }

  def prettyNull(): PrettyFragment = PrettyFragment(Seq("()"))

  def prettyEntry(entry: (Devon, Devon)): PrettyFragment = {
    val (key, value) = entry
    val prettyKey = prettyDevon(key)
    val prettyValue = prettyDevon(value)
    if (prettyKey.lines.size == 1 && prettyValue.lines.size == 1) {
      PrettyFragment(Seq(prettyKey.lines.head + " " + prettyValue.lines.head))
    } else {
      PrettyFragment.combine(prettyKey, prettyValue)
    }
  }

  def prettyQuotedString(devon: DevonString): PrettyFragment = {
    val escaped = devon.string.replaceAll("\'", "\'\'")
    val wrapped = "\'" + escaped + "\'"
    val split = wrapped.split("\r\n|\r|\n").toSeq
    PrettyFragment(split)
  }

  def prettyUnquotedString(devon: DevonString): PrettyFragment = PrettyFragment(Seq(devon.string))

  def indent(line: String): String = s"  $line"
}
