
package com.seanshubin.devon.parserules

import com.seanshubin.devon.tokenrules._
import com.seanshubin.devon.parser.{Rule, RuleLookup}
import com.seanshubin.devon.rules._

class DevonRuleLookup extends RuleLookup[Token] {
  override def lookupRuleByName(name: String): Rule[Token] = rulesMap(name)

  private val rules: Seq[Rule[Token]] = Seq(
    ZeroOrMoreRule(this, "elements", "element"),
    OneOfRule(this, "element", "map", "array", "string", "null"),
    SequenceRule(this, "map", "begin-map", "pairs", "end-map"),
    SequenceRule(this, "array", "begin-array", "elements", "end-array"),
    ValueTypeRule(this, "string", classOf[TokenString]),
    ValueRule(this, "null", TokenNull),
    ValueRule(this, "begin-map", TokenOpenBrace),
    ZeroOrMoreRule(this, "pairs", "pair"),
    ValueRule(this, "end-map", TokenCloseBrace),
    ValueRule(this, "begin-array", TokenOpenBracket),
    ValueRule(this, "end-array", TokenCloseBracket),
    SequenceRule(this, "pair", "element", "element")
  )
  private val rulesMap: Map[String, Rule[Token]] = rules.map(rule => (rule.thisRuleName, rule)).toMap
}
