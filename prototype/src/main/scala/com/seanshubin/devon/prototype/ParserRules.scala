package com.seanshubin.devon.prototype

class ParserRules {
  def singleToken(token: Token): Rule[Token, AbstractSyntaxTree] = ???

  def oneOf(ruleNames: String*): Rule[Token, AbstractSyntaxTree] = ???

  def not(ruleName: String): Rule[Token, AbstractSyntaxTree] = ???

  def zeroOrMore(ruleName: String): Rule[Token, AbstractSyntaxTree] = ???

  def sequence(ruleNames: String*): Rule[Token, AbstractSyntaxTree] = ???

  def oneOrMore(ruleName: String): Rule[Token, AbstractSyntaxTree] = ???

  val rules: Map[String, Rule[Token, AbstractSyntaxTree]] = Map(
    "pair" -> sequence("element", "element"),
    "pairs" -> zeroOrMore("pair"),
    "object" -> sequence("open-brace", "pairs", "close-brace"),
    "array" -> sequence("open-bracket", "elements", "close-bracket"),
    "string" -> oneOf("quoted-contents", "unquoted-contents"),
    "element" -> oneOf("object", "array", "string", "null"),
    "elements" -> zeroOrMore("element")
  )
}
