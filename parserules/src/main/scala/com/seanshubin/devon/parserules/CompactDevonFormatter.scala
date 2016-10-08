package com.seanshubin.devon.parserules

trait CompactDevonFormatter {
  def format(devon: Devon): String

  def format(devonIterator: Iterator[Devon]): String
}
