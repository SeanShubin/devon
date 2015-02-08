package com.seanshubin.devon.core

import org.scalatest.FunSuite

class ReflectUtilTest extends FunSuite {
  test("create from primary constructor") {
    val values = Map("x" -> 1, "y" -> 2)
    val created: Point = ReflectUtil.create(classOf[Point], values)
    assert(created === Point(1, 2))
  }

  test("pull apart class") {
    val value = Point(1, 2)
    val map = ReflectUtil.pullApart(value)
    assert(map === Map("x" -> 1, "y" -> 2))
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
