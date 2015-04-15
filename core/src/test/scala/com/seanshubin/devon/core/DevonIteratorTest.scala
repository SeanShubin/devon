package com.seanshubin.devon.core

import com.seanshubin.devon.core.devon.{DevonMarshallerWiring, DevonString}
import org.scalatest.FunSuite

class DevonIteratorTest extends FunSuite {
  test("empty") {
    val text = ""
    val iterator = DevonMarshallerWiring.Default.charsToIterator(text.iterator)
    assert(!iterator.hasNext)
  }

  test("one thing") {
    val text = "foo"
    val iterator = DevonMarshallerWiring.Default.charsToIterator(text.iterator)
    assert(iterator.hasNext)
    assert(iterator.next === DevonString("foo"))
    assert(!iterator.hasNext)
  }

  test("two things") {
    val text = "foo bar"
    val iterator = DevonMarshallerWiring.Default.charsToIterator(text.iterator)
    assert(iterator.hasNext)
    assert(iterator.next === DevonString("foo"))
    assert(iterator.hasNext)
    assert(iterator.next === DevonString("bar"))
    assert(!iterator.hasNext)
  }

  test("invalid") {
    val text = "{"
    val iterator = DevonMarshallerWiring.Default.charsToIterator(text.iterator)
    val exception = intercept[RuntimeException] {
      iterator.hasNext
    }
    assert(exception.getMessage === "Could not match 'element', expected one of: map, array, string, null")
  }

  test("one thing then invalid") {
    val text = "foo {"
    val iterator = DevonMarshallerWiring.Default.charsToIterator(text.iterator)
    assert(iterator.hasNext)
    assert(iterator.next === DevonString("foo"))
    val exception = intercept[RuntimeException] {
      iterator.hasNext
    }
    assert(exception.getMessage === "Could not match 'element', expected one of: map, array, string, null")
  }
}

