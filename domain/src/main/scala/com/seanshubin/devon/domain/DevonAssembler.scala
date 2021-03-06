package com.seanshubin.devon.domain

import com.seanshubin.devon.parser.{Assembler, ParseTree, ParseTreeBranch, ParseTreeLeaf}
import com.seanshubin.devon.tokenizer.{Token, TokenNull, TokenString}

import scala.collection.immutable.ListMap

class DevonAssembler extends Assembler[Token, Devon] {
  override def assembleFromParseTree(parseTree: ParseTree[Token]): Devon = {
    extract(parseTree)
  }

  private def extract(parseTree: ParseTree[Token]): Devon = {
    parseTree match {
      case ParseTreeLeaf("string", TokenString(s)) => DevonString(s)
      case ParseTreeBranch("map", children) => DevonMap(extractObject(children))
      case ParseTreeBranch("array", children) => DevonArray(extractArray(children))
      case ParseTreeLeaf("null", TokenNull) => DevonNull
      case _ => throw new RuntimeException(s"Don't know how to assemble $parseTree")
    }
  }

  private def extractObject(children: List[ParseTree[Token]]): ListMap[Devon, Devon] = {
    val List(_, pairsParseTree, _) = children
    val pairs = extractPairs(pairsParseTree)
    val map = ListMap(pairs: _*)
    map
  }

  private def extractArray(children: List[ParseTree[Token]]): Seq[Devon] = {
    val List(_, ParseTreeBranch("elements", elements), _) = children
    elements.map(extract)
  }

  private def extractPair(parseTree: ParseTree[Token]): (Devon, Devon) = {
    val ParseTreeBranch("pair", List(firstParseTree, secondParseTree)) = parseTree
    val first = extract(firstParseTree)
    val second = extract(secondParseTree)
    (first, second)
  }

  private def extractPairs(parseTree: ParseTree[Token]): Seq[(Devon, Devon)] = {
    val ParseTreeBranch("pairs", list) = parseTree
    val result = list.map(extractPair)
    result
  }
}
