package com.seanshubin.devon.parser

trait RuleLookup[A] {
  def lookupRuleByName(name: String): Rule[A]
}
