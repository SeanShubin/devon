package com.seanshubin.devon.core.devon

trait CompactDevonFormatter {
  def format(devon: Devon): String

  def format(devonIterator: Iterator[Devon]): String
}
