package com.seanshubin.devon.core

class ParserIterator[A, B](backingIterator: Iterator[A], ruleLookup: RuleLookup[A], assembler: Assembler[A, B], ruleName: String, endValue:B) extends Iterator[B] {
  private var currentCursor = Cursor.fromIterator(backingIterator)
  private var currentMatchResult = parse(ruleName, currentCursor)

  override def hasNext: Boolean = {
    val MatchSuccess(parseTree, _) = currentMatchResult
    val token = assembler.assembleFromParseTree(parseTree)
    token != endValue
  }

  override def next(): B = {
    val MatchSuccess(parseTree, newCursor) = currentMatchResult
    val token = assembler.assembleFromParseTree(parseTree)
    currentCursor = newCursor
    currentMatchResult = parse(ruleName, currentCursor)
    token
  }

  private def parse(ruleName: String, cursor: Cursor[A]): MatchResult[A] = {
    val rule: Rule[A] = ruleLookup.lookupRuleByName(ruleName)
    val matchResult: MatchResult[A] = rule.apply(cursor)
    matchResult
  }
}
