package com.seanshubin.devon.core

object NoOperationStringProcessor extends StringProcessor {
  override def processedToRaw(s: String): String = s

  override def rawToProcessed(s: String): String = s
}
