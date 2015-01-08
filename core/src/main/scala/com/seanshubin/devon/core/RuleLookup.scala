package com.seanshubin.devon.core

trait RuleLookup[A, B] {
  def lookupRuleByName(name: String): Rule[A, B]
}
