package com.seanshubin.devon.prototype

import org.scalatest.FunSuite

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe.Symbol

class ScalaReflectionTest extends FunSuite {
  test("construct case class") {
    val mirror = universe.runtimeMirror(getClass.getClassLoader)
    val dynamicClass = Class.forName("com.seanshubin.devon.prototype.Sample")
    val classSymbol = mirror.classSymbol(dynamicClass)
    val classMirror = mirror.reflectClass(classSymbol)
    val sampleConstructor = classMirror.symbol.info.decl(universe.termNames.CONSTRUCTOR).asMethod
    val constructorMethod = classMirror.reflectConstructor(sampleConstructor)
    val sample = constructorMethod(1, 2)
    assert(sample === Sample(1, 2))
    println(sample)
  }

  test("read case class") {
    val sample: Any = new Sample(1, 2)
    val mirror = universe.runtimeMirror(getClass.getClassLoader)
    val dynamicClass = sample.getClass
    val classSymbol = mirror.classSymbol(dynamicClass)
    val classMirror = mirror.reflectClass(classSymbol)

    def isAccessor(symbol: Symbol): Boolean = {
      symbol.isPublic && symbol.isMethod && symbol.isTerm && !symbol.isConstructor && !symbol.isSynthetic
    }

    def displayValue(symbol: Symbol): String = {
      val methodSymbol = classMirror.symbol.info.decl(symbol.name).asMethod
      val instanceMirror = mirror.reflect(sample)
      val fieldMirror = instanceMirror.reflectField(methodSymbol)
      val fieldValue = fieldMirror.get
      val fieldType = fieldValue.getClass.getName
      val fieldName = symbol.name
      s"$fieldName = ($fieldType) $fieldValue"
    }

    val values = classMirror.symbol.info.decls.filter(isAccessor).map(displayValue).toSeq
    assert(values.size === 2)
    assert(values(0) === "x = (java.lang.Integer) 1")
    assert(values(1) === "y = (java.lang.Integer) 2")
  }
}
