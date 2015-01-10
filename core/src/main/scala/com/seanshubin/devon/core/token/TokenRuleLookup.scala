
package com.seanshubin.devon.core.token

import com.seanshubin.devon.core.rules._
import com.seanshubin.devon.core.{Rule, RuleLookup}

class TokenRuleLookup extends RuleLookup[Char] {
  override def lookupRuleByName(name: String): Rule[Char] = rulesMap(name)

  private val quote = '\''
  private val openBrace = '{'
  private val closeBrace = '}'
  private val openBracket = '['
  private val closeBracket = ']'
  private val openParen = '('
  private val closeParen = ')'
  private val space = ' '
  private val tab = '\t'
  private val carriageReturn = '\r'
  private val lineFeed = '\n'

  private val whitespace = Seq(space, tab, carriageReturn, lineFeed)
  private val structural = Seq(openBrace, closeBrace, openBracket, closeBracket, openParen, closeParen, quote)
  private val structuralAndWhitespace = structural ++ whitespace

  private val rules: Seq[Rule[Char]] = Seq(
    OneOfRule(this, "token",
      "open-brace", "close-brace", "open-bracket", "close-bracket", "null", "quoted-word", "unquoted-word",
      "whitespace-block", "end"),
    ValueRule(this, "open-brace", openBrace),
    ValueRule(this, "close-brace", closeBrace),
    ValueRule(this, "open-bracket", openBracket),
    ValueRule(this, "close-bracket", closeBracket),
    SequenceRule(this, "null", "open-paren", "close-paren"),
    SequenceRule(this, "quoted-word", "quote", "quoted-contents", "quote"),
    OneOrMoreRule(this, "unquoted-word", "unquoted-word-character"),
    OneOrMoreRule(this, "whitespace-block", "whitespace"),
    EndRule(this, "end"),
    ValueRule(this, "open-paren", openParen),
    ValueRule(this, "close-paren", closeParen),
    ValueRule(this, "quote", quote),
    OneOrMoreRule(this, "quoted-contents", "quoted-word-character"),
    ValueOtherThanRule(this, "unquoted-word-character", structuralAndWhitespace: _*),
    ValueRule(this, "whitespace", whitespace: _*),
    OneOfRule(this, "quoted-word-character", "not-quote", "two-quotes"),
    ValueOtherThanRule(this, "not-quote", quote),
    SequenceRule(this, "two-quotes", "quote", "quote")
  )
  private val rulesMap: Map[String, Rule[Char]] = rules.map(rule => (rule.thisRuleName, rule)).toMap
}
