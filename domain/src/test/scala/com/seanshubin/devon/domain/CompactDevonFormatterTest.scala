package com.seanshubin.devon.domain

import org.scalatest.FunSuite

class CompactDevonFormatterTest extends FunSuite {
  test("compact") {
    val arrayOfArray: Seq[Seq[String]] = Seq(Seq("a"), Seq("b c"))
    val arrayOfMap: Seq[Map[String, String]] = Seq(Map("a" -> "b c"))
    val mapOfArray: Map[Seq[String], Seq[String]] = Map(Seq("a", "b c") -> Seq("d e", "f"))
    val mapOfMap: Map[Map[String, String], Map[String, String]] = Map(Map("a" -> "b c") -> Map("d e" -> "f"))
    val strings: Seq[String] = Seq("a", "b c", "d e", "f", "g")
    val marshaller = DevonMarshallerWiring.Default
    assert(marshaller.valueToCompact(arrayOfArray) === "[[a]['b c']]")
    assert(marshaller.valueToCompact(arrayOfMap) === "[{a'b c'}]")
    assert(marshaller.valueToCompact(mapOfArray) === "{[a'b c']['d e'f]}")
    assert(marshaller.valueToCompact(mapOfMap) === "{{a'b c'}{'d e'f}}")
    assert(marshaller.valueToCompact(strings) === "[a'b c' 'd e'f g]")
  }
}
