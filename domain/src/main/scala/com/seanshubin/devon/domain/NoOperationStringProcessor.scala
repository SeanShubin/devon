package com.seanshubin.devon.domain

import com.seanshubin.devon.parser.StringProcessor

object NoOperationStringProcessor extends StringProcessor {
  override def processedToRaw(s: String): String = s

  override def rawToProcessed(s: String): String = s
}
