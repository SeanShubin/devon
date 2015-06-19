package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.token.TokenCharacters
import com.seanshubin.devon.parser.StringProcessor

class CompactDevonFormatterImpl(stringProcessor: StringProcessor) extends CompactDevonFormatter {

  import CompactFragment._

  def format(devon: Devon): String = {
    compactDevon(devon).text
  }

  def format(devonIterator: Iterator[Devon]): String = {
    compactArrayWithoutBrackets(devonIterator.toSeq).text
  }

  private def compactDevon(devon: Devon): CompactFragment = {
    devon match {
      case DevonNull => CompactFragment(Symbol, "()", Symbol)
      case DevonString(string) => compactDevonString(string)
      case DevonArray(array) => compactArray(array)
      case DevonMap(map) => compactMap(map)
      case x => throw new RuntimeException(s"Unable to format $x")
    }
  }

  private def compactDevonString(unprocessedString: String): CompactFragment = {
    val processedString = stringProcessor.rawToProcessed(unprocessedString)
    if (processedString.exists(x => TokenCharacters.structuralAndWhitespace.contains(x)) || processedString.isEmpty) {
      compactQuotedString(processedString)
    } else {
      compactUnquotedString(processedString)
    }
  }

  private def compactQuotedString(string: String): CompactFragment = {
    val escaped = string.replaceAll("\'", "\'\'")
    val wrapped = "\'" + escaped + "\'"
    CompactFragment(Quoted, wrapped, Quoted)
  }

  private def compactUnquotedString(string: String): CompactFragment = CompactFragment(Unquoted, string, Unquoted)

  private def compactArray(array: Seq[Devon]): CompactFragment = {
    if (array.isEmpty) CompactFragment(Symbol, "[]", Symbol)
    else {
      val text = "[" + array.map(compactDevon).reduceLeft(CompactFragment.merge).text + "]"
      CompactFragment(Symbol, text, Symbol)
    }
  }

  private def compactArrayWithoutBrackets(array: Seq[Devon]): CompactFragment = {
    val text = array.map(compactDevon).reduceLeft(CompactFragment.merge).text
    CompactFragment(Symbol, text, Symbol)
  }

  private def compactMap(map: Map[Devon, Devon]): CompactFragment = {
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

  private case class CompactFragment(left: Edge, text: String, right: Edge)

  private object CompactFragment {

    sealed trait Edge

    case object Unquoted extends Edge

    case object Quoted extends Edge

    case object Symbol extends Edge

    def merge(left: CompactFragment, right: CompactFragment): CompactFragment = {
      (left.right, right.left) match {
        case (Unquoted, Unquoted) =>
          CompactFragment(left.left, left.text + " " + right.text, right.right)
        case (Quoted, Quoted) =>
          CompactFragment(left.left, left.text + " " + right.text, right.right)
        case _ => CompactFragment(left.left, left.text + right.text, right.right)
      }
    }
  }

}
