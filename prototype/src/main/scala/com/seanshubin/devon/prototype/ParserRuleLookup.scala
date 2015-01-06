package com.seanshubin.devon.prototype

class ParserRuleLookup extends RuleLookup[Token, AbstractSyntaxTree] {
  override def lookupRuleByName(name: String): Rule[Token, AbstractSyntaxTree] = {
    rules(name)
  }

  def singleToken(token: Token): Rule[Token, AbstractSyntaxTree] = new SingleTokenRule(token)

  def oneOf(ruleNames: String*): Rule[Token, AbstractSyntaxTree] =
    new OneOfRule(ruleNames: _*)

  def not(ruleName: String): Rule[Token, AbstractSyntaxTree] = ???

  def zeroOrMore(ruleName: String): Rule[Token, AbstractSyntaxTree] =
    new ZeroOrMoreRule[Token, AbstractSyntaxTree](ruleName)

  def sequence(ruleNames: String*): Rule[Token, AbstractSyntaxTree] =
    new SequenceRule[Token, AbstractSyntaxTree](ruleNames: _*)

  def oneOrMore(ruleName: String): Rule[Token, AbstractSyntaxTree] = ???

  def tokenWord(): Rule[Token, AbstractSyntaxTree] = new TokenWordRule[Token, AbstractSyntaxTree]()

  val rules: Map[String, Rule[Token, AbstractSyntaxTree]] = Map(
    "open-brace" -> singleToken(TokenOpenBrace),
    "open-bracket" -> singleToken(TokenOpenBracket),
    "pair" -> sequence("element", "element"),
    "close-brace" -> singleToken(TokenCloseBrace),
    "pairs" -> zeroOrMore("pair"),
    "object" -> sequence("open-brace", "pairs", "close-brace"),
    "array" -> sequence("open-bracket", "elements", "close-bracket"),
    "string" -> tokenWord(),
    "null" -> singleToken(TokenNull),
    "element" -> oneOf("object", "array", "string", "null"),
    "elements" -> zeroOrMore("element")
  )
}
