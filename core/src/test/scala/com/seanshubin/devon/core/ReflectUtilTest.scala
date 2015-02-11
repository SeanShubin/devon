package com.seanshubin.devon.core

import org.scalatest.FunSuite

class ReflectUtilTest extends FunSuite {
  test("create from primary constructor") {
    val values = Map("x" -> "1", "y" -> "2")
    val created: Point = ReflectUtil.construct(values, classOf[Point])
    assert(created === Point(1, 2))
  }

  test("pull apart class") {
    val value = Point(1, 2)
    val map = ReflectUtil.pullApart(value)
    assert(map === Map("x" -> "1", "y" -> "2"))
  }

  test("create nested from primary constructor") {
    val values = Map("topLeft" -> Map("x" -> "1", "y" -> "2"), "bottomRight" -> Map("x" -> "3", "y" -> "4"))
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val rectangle = Rectangle(topLeft, bottomRight)
    val created: Rectangle = ReflectUtil.construct(values, classOf[Rectangle])
    assert(created === rectangle)
  }

  test("pull apart nested class") {
    val values = Map("topLeft" -> Map("x" -> "1", "y" -> "2"), "bottomRight" -> Map("x" -> "3", "y" -> "4"))
    val topLeft = Point(1, 2)
    val bottomRight = Point(3, 4)
    val rectangle = Rectangle(topLeft, bottomRight)
    val map = ReflectUtil.pullApart(rectangle)
    assert(map === values)
  }

  private def symbolToLines(symbol: reflect.runtime.universe.Symbol): Seq[String] = {
    Seq(
      s"$symbol",
      s"Abstract = ${symbol.isAbstract}",
      s"AbstractOverride = ${symbol.isAbstractOverride}",
      s"Constructor = ${symbol.isConstructor}",
      s"Private = ${symbol.isPrivate}",
      s"Implicit = ${symbol.isImplicit}",
      s"Class = ${symbol.isClass}",
      s"Java = ${symbol.isJava}",
      s"Final = ${symbol.isFinal}",
      s"ImplementationArtifact = ${symbol.isImplementationArtifact}",
      s"Macro = ${symbol.isMacro}",
      s"Method = ${symbol.isMethod}",
      s"Module = ${symbol.isModule}",
      s"ModuleClass = ${symbol.isModuleClass}",
      s"Package = ${symbol.isPackage}",
      s"PackageClass = ${symbol.isPackageClass}",
      s"Parameter = ${symbol.isParameter}",
      s"PrivateThis = ${symbol.isPrivateThis}",
      s"Protected = ${symbol.isProtected}",
      s"ProtectedThis = ${symbol.isProtectedThis}",
      s"Public = ${symbol.isPublic}",
      s"Specialized = ${symbol.isSpecialized}",
      s"Static = ${symbol.isStatic}",
      s"Synthetic = ${symbol.isSynthetic}",
      s"Term = ${symbol.isTerm}",
      s"Type = ${symbol.isType}",
      s""
    )
  }
}
