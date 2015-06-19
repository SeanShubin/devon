package com.seanshubin.devon.parser

class ParserIterator[A, B](backingIterator: Iterator[A], ruleLookup: RuleLookup[A], assembler: Assembler[A, B], ruleName: String) extends Iterator[B] {
  private var currentCursor = Cursor.fromIterator(backingIterator)
  private var currentMatchResult = parse(ruleName, currentCursor)

  override def hasNext: Boolean = {
    if (currentCursor.isEnd) {
      false
    } else {
      currentMatchResult match {
        case x: MatchSuccess[A] => true
        case MatchFailure(rule, message) =>
          throw new RuntimeException(s"Could not match '$rule', $message")
      }
    }
  }

  override def next(): B = {
    currentMatchResult match {
      case MatchSuccess(parseTree, newCursor) =>
        val token = assembler.assembleFromParseTree(parseTree)
        currentCursor = newCursor
        currentMatchResult = parse(ruleName, currentCursor)
        token
      case MatchFailure(rule, message) =>
        throw new RuntimeException(s"Could not match '$rule', $message")
    }
  }

  private def parse(ruleName: String, cursor: Cursor[A]): MatchResult[A] = {
    val rule: Rule[A] = ruleLookup.lookupRuleByName(ruleName)
    val matchResult: MatchResult[A] = rule.apply(cursor)
    matchResult
  }
}
