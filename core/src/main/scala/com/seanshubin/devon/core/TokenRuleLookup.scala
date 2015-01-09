package com.seanshubin.devon.core

import com.seanshubin.devon.core.rules._

class TokenRuleLookup extends RuleLookup[Char] {
  override def lookupRuleByName(name: String): Rule[Char] = rulesMap(name)

  private val whitespace = Seq(' ', '\t', '\r', '\n')
  private val structural = Seq('{', '}', '[', ']', '(', ')', '\'')
  private val structuralAndWhitespace = structural ++ whitespace

  private val rules: Seq[Rule[Char]] = Seq(
    OneOfRule(this, "token", "open-brace", "close-brace", "open-bracket", "close-bracket", "null", "quoted-word", "unquoted-word", "whitespace-block"),
    ValueRule(this, "open-brace", '{'),
    ValueRule(this, "close-brace", '}'),
    ValueRule(this, "open-bracket", '['),
    ValueRule(this, "close-bracket", ']'),
    ValueRule(this, "open-paren", '('),
    ValueRule(this, "close-paren", ')'),
    ValueRule(this, "quote", '\''),
    ValueOtherThanRule(this, "unquoted-word-character", structuralAndWhitespace),
    ValueOtherThanRule(this, "not-quote", Seq('\'')),
    OneOrMoreRule(this, "unquoted-word", "unquoted-word-character"),
    ValueRule(this, "whitespace", whitespace: _*),
    OneOrMoreRule(this, "whitespace-block", "whitespace"),
    SequenceRule(this, "null", "open-paren", "close-paren"),
    SequenceRule(this, "quoted-word", "quote", "quoted-contents", "quote"),
    OneOrMoreRule(this, "quoted-contents", "quoted-word-character"),
    OneOfRule(this, "quoted-word-character", "not-quote", "two-quotes"),
    SequenceRule(this, "two-quotes", "quote", "quote")
  )
  private val rulesMap: Map[String, Rule[Char]] = rules.map(rule => (rule.thisRuleName, rule)).toMap
}
