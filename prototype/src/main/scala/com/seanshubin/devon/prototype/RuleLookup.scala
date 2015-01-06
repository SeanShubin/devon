package com.seanshubin.devon.prototype

trait RuleLookup[A, B] {
  def lookupRuleByName(name: String): Rule[A, B]
}
