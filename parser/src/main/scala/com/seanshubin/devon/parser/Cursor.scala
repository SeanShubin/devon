package com.seanshubin.devon.parser

import com.seanshubin.devon.string.StringUtil

trait Cursor[T] {
  def next: Cursor[T]

  def value: T

  def isEnd: Boolean
}

object Cursor {
  def fromIterator[T](iterator: Iterator[T]): Cursor[T] = new CursorBackedByIterator[T](iterator)

  def values[T](begin: Cursor[T], afterEnd: Cursor[T]): Seq[T] = {
    def loop(soFar: List[T], current: Cursor[T]): List[T] = {
      if (current == afterEnd) soFar
      else loop(current.value :: soFar, current.next)
    }
    val reversed = loop(Nil, begin)
    val theValues = reversed.reverse
    theValues
  }

  private class CursorBackedByIterator[T](iterator: Iterator[T]) extends Cursor[T] {
    private val maybeValue: Option[T] =
      if (iterator.hasNext) Some(iterator.next())
      else None
    private var maybeNext: Option[Cursor[T]] = None

    override def next: Cursor[T] = maybeNext match {
      case Some(nextCursor) => nextCursor
      case None =>
        val nextCursor = new CursorBackedByIterator(iterator)
        maybeNext = Some(nextCursor)
        nextCursor
    }

    override def value: T = maybeValue.get

    override def isEnd: Boolean = maybeValue.isEmpty

    private def escapedValue: Option[String] = maybeValue.map(x => StringUtil.escape(x.toString))

    override def toString: String = s"Cursor(value=$escapedValue, isEnd=$isEnd)"
  }

}
