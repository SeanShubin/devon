package com.seanshubin.devon.prototype

case class ElementAssembler(composed: List[AbstractSyntaxTree]) extends Assembler[Token, AbstractSyntaxTree] {
  override def top: AbstractSyntaxTree = composed.head

  override def update(ruleName: String, cursorBegin: Cursor[Token], cursorEnd: Cursor[Token]): Assembler[Token, AbstractSyntaxTree] = {
    val values = Cursor.values(cursorBegin, cursorEnd)
    val result: Assembler[Token, AbstractSyntaxTree] = if (ruleName == "single token" && values == Seq(TokenOpenBrace)) {
      copy(AstBeginMap :: composed)
    } else if (ruleName == "string") {
      val Seq(TokenWord(stringValue)) = values
      copy(AstString(stringValue) :: composed)
    } else if (ruleName == "single token" && values == Seq(TokenCloseBrace)) {
      createObject()
    } else {
      unsupported(ruleName, values)
    }
    result
  }

  def createObject(): Assembler[Token, AbstractSyntaxTree] = {
    val (reversedValues, remain) = composed.span(_ != AstBeginMap)
    val values = reversedValues.reverse
    def pairToTuple(pair: List[AbstractSyntaxTree]): (AbstractSyntaxTree, AbstractSyntaxTree) = (pair.head, pair.tail.head)
    val valuePairs = values.grouped(2)
    val tuples = valuePairs.map(pairToTuple)
    val map = tuples.toMap
    copy(AstObject(map) :: remain.tail)
  }

  def unsupported(ruleName: String, values: Seq[Token]): Assembler[Token, AbstractSyntaxTree] = {
    throw new RuntimeException(s"unsupported rule $ruleName with values $values")
  }
}
