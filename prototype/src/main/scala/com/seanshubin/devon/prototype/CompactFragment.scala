package com.seanshubin.devon.prototype

import com.seanshubin.devon.prototype.CompactFragment.Edge

case class CompactFragment(left: Edge, text: String, right: Edge)

object CompactFragment {

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
