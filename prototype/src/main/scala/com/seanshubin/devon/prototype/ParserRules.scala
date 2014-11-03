package com.seanshubin.devon.prototype

class ParserRules {
  def singleToken(token: Token): Rule[AbstractSyntaxTree] = ???

  def oneOf(ruleNames: String*): Rule[AbstractSyntaxTree] = ???

  def not(ruleName: String): Rule[AbstractSyntaxTree] = ???

  def zeroOrMore(ruleName: String): Rule[AbstractSyntaxTree] = ???

  def sequence(ruleNames: String*): Rule[AbstractSyntaxTree] = ???

  def oneOrMore(ruleName: String): Rule[AbstractSyntaxTree] = ???

  val rules: Map[String, Rule[AbstractSyntaxTree]] = Map(
    "pair" -> sequence("element", "element"),
    "pairs" -> zeroOrMore("pair"),
    "object" -> sequence("open-brace", "pairs", "close-brace"),
    "array" -> sequence("open-bracket", "elements", "close-bracket"),
    "string" -> oneOf("quoted-contents", "unquoted-contents"),
    "element" -> oneOf("object", "array", "string", "null"),
    "elements" -> zeroOrMore("element")
  )
}
