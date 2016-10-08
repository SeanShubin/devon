package com.seanshubin.devon.domain

trait CompactDevonFormatter {
  def format(devon: Devon): String

  def format(devonIterator: Iterator[Devon]): String
}
