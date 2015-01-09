package com.seanshubin.devon.core

import com.seanshubin.devon.core.rules._

class TokenRuleLookup extends RuleLookup[Char] {
  override def lookupRuleByName(name: String): Rule[Char] = rulesMap(name)

  private val rules: Seq[Rule[Char]] = Seq(
    OneOfRule("token", this, "open-brace", "close-brace", "open-bracket", "close-bracket", "null", "quoted-word", "unquoted-word", "whitespace-block"),
    ValueRule("open-brace", this, '{'),
    ValueRule("close-brace", this, '}'),
    ValueRule("open-bracket", this, '['),
    ValueRule("close-bracket", this, ']'),
    ValueRule("open-paren", this, '('),
    ValueRule("close-paren", this, ')'),
    SequenceRule("null", this, "open-paren", "close-paren"))
  private val rulesMap: Map[String, Rule[Char]] = rules.map(rule => (rule.thisRuleName, rule)).toMap
}
