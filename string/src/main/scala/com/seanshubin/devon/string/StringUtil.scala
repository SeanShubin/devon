package com.seanshubin.devon.string

import scala.annotation.tailrec

object StringUtil {
  def escape(target: String) = {
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
    @tailrec
    def unescapeRemaining(soFar: String, remain: String): String = {
      if (remain.isEmpty) {
        soFar
      } else {
        val ch = remain.head
        if (ch == '\\') {
          unescapeAfterBackslash(soFar, remain.tail)
        } else {
          unescapeRemaining(soFar + ch, remain.tail)
        }
      }
    }

    def unescapeAfterBackslash(soFar: String, remain: String): String = {
      if (remain.isEmpty) {
        throw new RuntimeException("end of string encountered after backslash")
      } else {
        val ch = remain.head
        val escapeCh = ch match {
          case 'n' => '\n'
          case 'b' => '\b'
          case 't' => '\t'
          case 'f' => '\f'
          case 'r' => '\r'
          case '\"' => '\"'
          case '\'' => '\''
          case '\\' => '\\'
          case x => throw new RuntimeException("Unsupported escape sequence: " + ch)
        }
        unescapeRemaining(soFar + escapeCh, remain.tail)
      }
    }
    unescapeRemaining("", target)
  }
}
