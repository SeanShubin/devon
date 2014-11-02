package com.seanshubin.devon.prototype

class ParserRules {
  def singleToken(token: Token): Rule = ???

  def oneOf(ruleNames: String*): Rule = ???

  def not(ruleName: String): Rule = ???

  def zeroOrMore(ruleName: String): Rule = ???

  def sequence(ruleNames: String*): Rule = ???

  def oneOrMore(ruleName: String): Rule = ???

  val rules: Map[String, Rule] = Map(
    "pair" -> sequence("element", "element"),
    "pairs" -> zeroOrMore("pair"),
    "object" -> sequence("open-brace", "pairs", "close-brace"),
    "array" -> sequence("open-bracket", "elements", "close-bracket"),
    "string" -> oneOf("quoted-contents", "unquoted-contents"),
    "element" -> oneOf("object", "array", "string", "null"),
    "elements" -> zeroOrMore("element")
  )
}
