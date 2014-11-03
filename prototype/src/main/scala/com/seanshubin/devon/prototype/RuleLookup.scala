package com.seanshubin.devon.prototype

trait RuleLookup[A] {
  def lookupByName(name:String):Rule[A]
}
