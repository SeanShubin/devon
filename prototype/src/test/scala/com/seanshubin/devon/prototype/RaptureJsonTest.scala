package com.seanshubin.devon.prototype

import org.scalatest.FunSuite
import rapture.json._
import jsonBackends.scalaJson._

class RaptureJsonTest extends FunSuite {
  test("create nested from primary constructor") {
    val values = Map("topLeft" -> Map("x" -> 1, "y" -> 2), "bottomRight" -> Map("x" -> 3, "y" -> 4))
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val rectangle = Rectangle(topLeft, bottomRight)

    val json =  Json(values)
    val created = json.as[Rectangle]
    assert(created === rectangle)
  }
}
