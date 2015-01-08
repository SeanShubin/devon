package com.seanshubin.devon.core

trait Rule[A] {
  def apply(cursor: Cursor[A]): MatchResult[A]
}
