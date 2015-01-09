package com.seanshubin.devon.core

sealed trait ParseTree[A] {
  def name: String
}

case class ParseTreeLeaf[A](name: String, value: A) extends ParseTree[A]

case class ParseTreeBranch[A](name: String, children: List[ParseTree[A]]) extends ParseTree[A]
