package com.seanshubin.devon.core

import com.seanshubin.devon.core.rules.{NotValueRule, OneOrMoreRule}

class TokenRuleLookup extends RuleLookup[Char] {
  override def lookupRuleByName(name: String): Rule[Char] = rules(name)

  private val rules: Map[String, Rule[Char]] = Map(
    "unquoted-word" -> OneOrMoreRule("unquoted-word", this, "not-space"),
    "not-space" -> NotValueRule("not-space", this, Seq(' '))
  )
}
