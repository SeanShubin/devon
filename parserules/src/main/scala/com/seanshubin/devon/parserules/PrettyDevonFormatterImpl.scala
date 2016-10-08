package com.seanshubin.devon.parserules

import com.seanshubin.devon.tokenrules.TokenCharacters
import com.seanshubin.devon.parser.StringProcessor

class PrettyDevonFormatterImpl(stringProcessor: StringProcessor) extends PrettyDevonFormatter {
  def format(devon: Devon): Seq[String] = {
    prettyDevon(devon).lines
  }

  private def prettyDevon(devon: Devon): PrettyFragment = {
    devon match {
      case x: DevonString => prettyString(x)
      case x: DevonMap => prettyMap(x)
      case x: DevonArray => prettyArray(x)
      case DevonNull => prettyNull()
      case x => throw new RuntimeException(s"unable to pretty format: $x")
    }
  }

  private def prettyString(devon: DevonString): PrettyFragment = {
    val string = stringProcessor.rawToProcessed(devon.string)
    if (string.exists(x => TokenCharacters.structuralAndWhitespace.contains(x)) || string.isEmpty) {
      prettyQuotedString(string)
    } else {
      prettyUnquotedString(string)
    }
  }

  private def prettyMap(devon: DevonMap): PrettyFragment = {
    if (devon.map.size == 0) PrettyFragment(Seq("{}"))
    else {
      val fragments: Seq[PrettyFragment] = devon.map.map(prettyEntry).toSeq
      val combined: PrettyFragment = fragments.reduceLeft(PrettyFragment.combine)
      PrettyFragment(Seq("{") ++ combined.lines.map(indent) ++ Seq("}"))
    }
  }

  private def prettyArray(devon: DevonArray): PrettyFragment = {
    if (devon.array.size == 0) PrettyFragment(Seq("[]"))
    else {
      val fragments: Seq[PrettyFragment] = devon.array.map(prettyDevon)
      val combined: PrettyFragment = fragments.reduceLeft(PrettyFragment.combine)
      PrettyFragment(Seq("[") ++ combined.lines.map(indent) ++ Seq("]"))
    }
  }

  private def prettyNull(): PrettyFragment = PrettyFragment(Seq("()"))

  private def prettyEntry(entry: (Devon, Devon)): PrettyFragment = {
    val (key, value) = entry
    val prettyKey = prettyDevon(key)
    val prettyValue = prettyDevon(value)
    if (prettyKey.lines.size == 1 && prettyValue.lines.size == 1) {
      PrettyFragment(Seq(prettyKey.lines.head + " " + prettyValue.lines.head))
    } else {
      PrettyFragment.combine(prettyKey, prettyValue)
    }
  }

  private def prettyQuotedString(string: String): PrettyFragment = {
    val escaped = string.replaceAll("\'", "\'\'")
    val wrapped = "\'" + escaped + "\'"
    val split = wrapped.split("\r\n|\r|\n").toSeq
    PrettyFragment(split)
  }

  private def prettyUnquotedString(string: String): PrettyFragment = PrettyFragment(Seq(string))

  private def indent(line: String): String = s"  $line"

  private case class PrettyFragment(lines: Seq[String])

  private object PrettyFragment {
    def combine(left: PrettyFragment, right: PrettyFragment): PrettyFragment = {
      PrettyFragment(left.lines ++ right.lines)
    }
  }

}
