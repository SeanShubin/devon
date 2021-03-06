package com.seanshubin.devon.domain

import org.scalatest.FunSuite

class MalformedTest extends FunSuite {
  test("open brace only") {
    val text = "{"
    val devonMarshaller = DevonMarshallerWiring.Default
    val exception = intercept[RuntimeException] {
      devonMarshaller.fromString(text)
    }
    val expectedMessage = "Could not match 'element', expected one of: map, array, string, null"
    assert(exception.getMessage === expectedMessage)
  }
}
