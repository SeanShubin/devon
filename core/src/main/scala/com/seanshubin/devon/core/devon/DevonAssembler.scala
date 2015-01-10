package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.token.Token
import com.seanshubin.devon.core.{Assembler, ParseTree}

class DevonAssembler extends Assembler[Token, Devon] {
  override def assembleFromParseTree(parseTree: ParseTree[Token]): Devon = {
    println(parseTree)
    assemblers(parseTree.name)(parseTree)
  }

  private def extractMap(parseTree: ParseTree[Token]): Map[Devon, Devon] = {
    println("map")
    println(parseTree)
    ???
  }

  private def extractArray(parseTree: ParseTree[Token]): Seq[Devon] = {
    println("array")
    println(parseTree)
    ???
  }

  private def extractString(parseTree: ParseTree[Token]): String = {
    println("string")
    println(parseTree)
    ???
  }

  private val assemblers: Map[String, ParseTree[Token] => Devon] = Map(
    "object" -> (parseTree => DevonMap(extractMap(parseTree))),
    "array" -> (parseTree => DevonArray(extractArray(parseTree))),
    "string" -> (parseTree => DevonString(extractString(parseTree))),
    "null" -> (parseTree => DevonNull)
  )
}
