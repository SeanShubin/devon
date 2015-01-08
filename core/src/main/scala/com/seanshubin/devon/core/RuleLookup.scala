package com.seanshubin.devon.core

trait RuleLookup[A] {
  def lookupRuleByName(name: String): Rule[A]
}
