package com.seanshubin.devon.parser

object NoOperationStringProcessor extends StringProcessor {
  override def processedToRaw(s: String): String = s

  override def rawToProcessed(s: String): String = s
}
