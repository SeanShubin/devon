package com.seanshubin.devon.core

import com.seanshubin.devon.core.rules._

class TokenRuleLookup extends RuleLookup[Char] {
  override def lookupRuleByName(name: String): Rule[Char] = rules(name)

  private val rules: Map[String, Rule[Char]] = Map(
    "token" -> OneOfRule("token", this, "open-brace", "close-brace", "open-bracket", "close-bracket", "null", "quoted-word", "unquoted-word", "whitespace-block"),
    "open-brace" -> ValueRule("open-brace", this, '{'),
    "close-brace" -> ValueRule("close-brace", this, '}'),
    "open-bracket" -> ValueRule("open-bracket", this, '['),
    "close-bracket" -> ValueRule("close-bracket", this, ']'),
    "open-paren" -> ValueRule("open-bracket", this, '('),
    "close-paren" -> ValueRule("close-bracket", this, ')'),
    "null" -> SequenceRule("null", this, "open-paren", "close-paren")
//    ,
//    "unquoted-word" -> OneOrMoreRule("unquoted-word", this, "not-space"),
//    "not-space" -> NotValueRule("not-space", this, Seq(' '))
  )
}
