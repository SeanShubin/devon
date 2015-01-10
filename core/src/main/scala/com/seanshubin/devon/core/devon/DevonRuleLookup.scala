
package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.rules._
import com.seanshubin.devon.core.token.{Token, TokenWord}
import com.seanshubin.devon.core.{Rule, RuleLookup}

class DevonRuleLookup extends RuleLookup[Token] {
  override def lookupRuleByName(name: String): Rule[Token] = rulesMap(name)

  private val rules: Seq[Rule[Token]] = Seq(
    ZeroOrMoreRule(this, "elements", "element"),
    OneOfRule(this, "element", "object", "array", "string", "null"),
    SequenceRule(this, "object", "begin-object", "pairs", "end-object"),
    ZeroOrMoreRule(this, "pairs", "pair"),
    SequenceRule(this, "pair", "element", "element"),
    SequenceRule(this, "array", "begin-array", "elements", "end-array"),
    ValueTypeRule(this, "open-brace", classOf[TokenWord])
  )
  private val rulesMap: Map[String, Rule[Token]] = rules.map(rule => (rule.thisRuleName, rule)).toMap
}
