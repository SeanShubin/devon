package com.seanshubin.devon.prototype

case class ElementAssembler(composed:List[AbstractSyntaxTree]) extends Assembler[Token, AbstractSyntaxTree] {
  override def top: AbstractSyntaxTree = composed.head

  override def push(value: Token): Assembler[Token, AbstractSyntaxTree] = copy(AstToken(value) :: composed)

  private val pushNull:AssembleCommand = (name, token) => copy(AstNull :: composed)
  private val pushObjectBegin:AssembleCommand = (name, token) => copy(AstBeginMap :: composed)
  private val createObject:AssembleCommand = (name, token) => {
    val (reversedValues, remain) = composed.span(_ != AstBeginMap)
    val values = reversedValues.reverse
    def pairToTuple(pair: List[AbstractSyntaxTree]): (AbstractSyntaxTree, AbstractSyntaxTree) = (pair.head, pair.tail.head)
    val valuePairs = values.grouped(2)
    val tuples = valuePairs.map(pairToTuple)
    val map = tuples.toMap
    copy(AstObject(map) :: remain.tail)
  }
  private val pushArrayBegin:AssembleCommand = (name, token) => copy(AstBeginArray :: composed)
  private val createArray:AssembleCommand = (name, token) => {
    val (reversedValues, remain) = composed.span(_ != AstBeginArray)
    val values = reversedValues.reverse
    copy(AstArray(values) :: remain.tail)
  }
  private val pushString:AssembleCommand = (name, token) => {
    val TokenWord(value) = token
    copy(AstString(value) :: composed)
  }
  private val doNothing:AssembleCommand = (name, token) => this

  private val commands:Map[String, AssembleCommand] = Map(
    "null" -> pushNull,
    "begin-object" -> pushObjectBegin,
    "end-object" -> createObject,
    "begin-array" -> pushArrayBegin,
    "end-array" -> createArray,
    "string" -> pushString).withDefaultValue(doNothing)
}
