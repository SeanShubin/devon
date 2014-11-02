package com.seanshubin.devon.prototype

sealed trait AbstractSyntaxTree

case class AstObject(map: Map[AbstractSyntaxTree, AbstractSyntaxTree]) extends AbstractSyntaxTree

case class AstArray(seq: Seq[AbstractSyntaxTree]) extends AbstractSyntaxTree

case class AstString(value: String) extends AbstractSyntaxTree

case object AstNull extends AbstractSyntaxTree