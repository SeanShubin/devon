package com.seanshubin.devon.prototype

class TokenRules {
  def singleChar(ch: Char): Rule[Char, Token] = ???

  def oneOf(ruleNames: String*): Rule[Char, Token] = ???

  def not(ruleName: String): Rule[Char, Token] = ???

  def zeroOrMore(ruleName: String): Rule[Char, Token] = ???

  def sequence(ruleNames: String*): Rule[Char, Token] = ???

  def oneOrMore(ruleName: String): Rule[Char, Token] = ???

  val rules: Map[String, Rule[Char, Token]] = Map(
    "open-brace" -> singleChar('{'),
    "close-brace" -> singleChar('}'),
    "open-bracket" -> singleChar('['),
    "close-bracket" -> singleChar(']'),
    "open-paren" -> singleChar('('),
    "close-paren" -> singleChar(')'),
    "space" -> singleChar(' '),
    "quote" -> singleChar('\''),
    "tab" -> singleChar('\t'),
    "return" -> singleChar('\r'),
    "newline" -> singleChar('\n'),
    "whitespace" -> oneOf("space", "tab", "return", "newline"),
    "structural" -> oneOf("open-brace", "close-brace", "open-bracket", "close-bracket", "open-paren", "close-paren", "quote"),
    "structural-or-whitespace" -> oneOf("structural", "whitespace"),
    "unquoted-word-character" -> not("structural-or-whitespace"),
    "not-quote" -> not("quote"),
    "null" -> sequence("open-paren", "close-paren"),
    "two-quotes" -> sequence("quote", "quote"),
    "quoted-word-character" -> oneOf("not-quote", "two-quotes"),
    "quoted-contents" -> oneOrMore("quoted-word-character"),
    "quoted-word" -> sequence("quote", "quoted-contents", "quote"),
    "unquoted-word" -> oneOrMore("unquoted-word-character"),
    "whitespace-block" -> oneOrMore("whitespace"),
    "word" -> oneOf("quoted-word", "unquoted-word"),
    "token" -> oneOf("open-brace", "close-brace", "open-bracket", "close-bracket", "null", "word", "whitespace-block"),
    "tokens" -> zeroOrMore("token")
  )
}
