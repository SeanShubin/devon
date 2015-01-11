
package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.rules._
import com.seanshubin.devon.core.token._
import com.seanshubin.devon.core.{Rule, RuleLookup}

class DevonRuleLookup extends RuleLookup[Token] {
  override def lookupRuleByName(name: String): Rule[Token] = rulesMap(name)

  private val rules: Seq[Rule[Token]] = Seq(
    ZeroOrMoreRule(this, "elements", "element"),
    OneOfRule(this, "element", "map", "array", "string", "null", "end"),
    SequenceRule(this, "map", "begin-object", "pairs", "end-object"),
    SequenceRule(this, "array", "begin-array", "elements", "end-array"),
    ValueTypeRule(this, "string", classOf[TokenString]),
    ValueRule(this, "null", TokenNull),
    ValueRule(this, "begin-object", TokenOpenBrace),
    ZeroOrMoreRule(this, "pairs", "pair"),
    ValueRule(this, "end-object", TokenCloseBrace),
    ValueRule(this, "begin-array", TokenOpenBracket),
    ValueRule(this, "end-array", TokenCloseBracket),
    SequenceRule(this, "pair", "element", "element"),
    EndRule(this, "end")
  )
  private val rulesMap: Map[String, Rule[Token]] = rules.map(rule => (rule.thisRuleName, rule)).toMap
}
