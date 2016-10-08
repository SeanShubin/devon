package com.seanshubin.devon.domain

trait PrettyDevonFormatter {
  def format(devon: Devon): Seq[String]
}
