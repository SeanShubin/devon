package com.seanshubin.devon.parser

object StringUtil {
  def escape(target: String): String = {
    target.flatMap {
      case '\n' => "\\n"
      case '\b' => "\\b"
      case '\t' => "\\t"
      case '\f' => "\\f"
      case '\r' => "\\r"
      case '\"' => "\\\""
      case '\'' => "\\\'"
      case '\\' => "\\\\"
      case x => x.toString
    }
  }

  def unescape(target: String): String = {
    target.
      replaceAll( """\\n""", "\n").
      replaceAll( """\\b""", "\b").
      replaceAll( """\\t""", "\t").
      replaceAll( """\\f""", "\f").
      replaceAll( """\\r""", "\r").
      replaceAll( """\\"""", "\"").
      replaceAll( """\\'""", "\'").
      replaceAll( """\\\\""", "\\")
  }

  def seqToString[A](values: Seq[A]) = values.map(_.toString).map(escape).mkString("\'", "\', \'", "\'")
}
