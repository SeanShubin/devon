package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.token.{Token, TokenNull, TokenWord}
import com.seanshubin.devon.core.{Assembler, ParseTree, ParseTreeBranch, ParseTreeLeaf}

class DevonAssembler extends Assembler[Token, Devon] {
  override def assembleFromParseTree(parseTree: ParseTree[Token]): Devon = {
    extract(parseTree)
  }

  private def extract(parseTree: ParseTree[Token]): Devon = {
    parseTree match {
      case ParseTreeLeaf("string", TokenWord(s)) => DevonString(s)
      case ParseTreeBranch("object", children) => DevonMap(extractObject(children))
      case ParseTreeBranch("array", children) => DevonArray(extractArray(children))
      case ParseTreeLeaf("null", TokenNull) => DevonNull
      case ParseTreeBranch("end", Nil) => DevonEnd
      case _ => throw new RuntimeException(s"Don't know how to assemble $parseTree")
    }
  }

  private def extractObject(children: List[ParseTree[Token]]): Map[Devon, Devon] = {
    val List(_, pairsParseTree, _) = children
    val pairs = extractPairs(pairsParseTree)
    val map = pairs.toMap
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
