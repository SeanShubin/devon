package com.seanshubin.devon.domain

import com.seanshubin.devon.parser.StringProcessor
import com.seanshubin.devon.string.StringUtil

object EscapeStringProcessor extends StringProcessor {
  override def processedToRaw(s: String): String = StringUtil.unescape(s)

  override def rawToProcessed(s: String): String = StringUtil.escape(s)
}
