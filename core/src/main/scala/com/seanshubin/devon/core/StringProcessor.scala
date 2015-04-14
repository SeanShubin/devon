package com.seanshubin.devon.core

trait StringProcessor {
  def processedToRaw(s: String): String

  def rawToProcessed(s: String): String
}
