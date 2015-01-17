package com.seanshubin.devon.prototype

case class PrettyFragment(lines: Seq[String])

object PrettyFragment {
  def combine(left: PrettyFragment, right: PrettyFragment): PrettyFragment = {
    PrettyFragment(left.lines ++ right.lines)
  }
}
