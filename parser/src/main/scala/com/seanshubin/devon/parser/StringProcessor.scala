package com.seanshubin.devon.parser

trait StringProcessor {
  def processedToRaw(s: String): String

  def rawToProcessed(s: String): String
}
