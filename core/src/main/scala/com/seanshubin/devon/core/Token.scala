package com.seanshubin.devon.core

sealed trait Token

case class TokenWord(word: String) extends Token
