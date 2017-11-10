package com.seanshubin.devon.tokenizer

import com.seanshubin.devon.parser.{Rule, RuleLookup}
import com.seanshubin.devon.rules._

class TokenRuleLookup extends RuleLookup[Char] {

  import com.seanshubin.devon.tokenizer.TokenCharacters._

  override def lookupRuleByName(name: String): Rule[Char] = rulesMap(name)

  private val rules: Seq[Rule[Char]] = Seq(
    OneOfRule(this, "token",
      "open-brace", "close-brace", "open-bracket", "close-bracket", "null", "quoted-string", "unquoted-string",
      "whitespace-block", "end"),
    ValueRule(this, "open-brace", openBrace),
    ValueRule(this, "close-brace", closeBrace),
    ValueRule(this, "open-bracket", openBracket),
    ValueRule(this, "close-bracket", closeBracket),
    SequenceRule(this, "null", "open-paren", "close-paren"),
    SequenceRule(this, "quoted-string", "quote", "quoted-contents", "quote"),
    OneOrMoreRule(this, "unquoted-string", "unquoted-string-character"),
    OneOrMoreRule(this, "whitespace-block", "whitespace"),
    EndRule(this, "end"),
    ValueRule(this, "open-paren", openParen),
    ValueRule(this, "close-paren", closeParen),
    ValueRule(this, "quote", quote),
    ZeroOrMoreRule(this, "quoted-contents", "quoted-string-character"),
    ValueOtherThanRule(this, "unquoted-string-character", structuralAndWhitespace: _*),
    ValueRule(this, "whitespace", whitespace: _*),
    OneOfRule(this, "quoted-string-character", "not-quote", "two-quotes"),
    ValueOtherThanRule(this, "not-quote", quote),
    SequenceRule(this, "two-quotes", "quote", "quote")
  )
  private val rulesMap: Map[String, Rule[Char]] = rules.map(rule => (rule.thisRuleName, rule)).toMap
}
