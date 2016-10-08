package com.seanshubin.devon.parserules

trait PrettyDevonFormatter {
  def format(devon: Devon): Seq[String]
}
