package com.seanshubin.devon.core

import com.seanshubin.devon.core.rules.{NotValueRule, OneOrMoreRule}

class TokenRuleLookup extends RuleLookup[Char, Token] {
  override def lookupRuleByName(name: String): Rule[Char, Token] = rules(name)

  private val rules: Map[String, Rule[Char, Token]] = Map(
    "unquoted-word" -> OneOrMoreRule("not-space"),
    "not-space" -> NotValueRule(Seq(' '))
  )
}
