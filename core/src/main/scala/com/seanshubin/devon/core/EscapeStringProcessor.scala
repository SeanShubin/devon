package com.seanshubin.devon.core

object EscapeStringProcessor extends StringProcessor {
  override def processedToRaw(s: String): String = StringUtil.unescape(s)

  override def rawToProcessed(s: String): String = StringUtil.escape(s)
}
