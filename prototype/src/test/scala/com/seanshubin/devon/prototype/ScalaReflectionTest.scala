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

  test("construct nested case class") {
    val mirror = universe.runtimeMirror(getClass.getClassLoader)
    val expectedTopLeft = Point(1, 2)
    val expectedBottomRight = Point(3, 4)
    val expectedRectangle = Rectangle(expectedTopLeft, expectedBottomRight)
    val rectangleTypeTag = universe.typeTag[Rectangle]
    val rectangleConstructor = rectangleTypeTag.tpe.decl(universe.termNames.CONSTRUCTOR).asMethod
    val topLeftSymbol: universe.Symbol = rectangleConstructor.typeSignature.paramLists.head.head
    val bottomRightSymbol: universe.Symbol = rectangleConstructor.typeSignature.paramLists.head.tail.head
    val rectangleClassSymbol: universe.ClassSymbol = rectangleTypeTag.tpe.typeSymbol.asClass
    val pointClassSymbol: universe.ClassSymbol = topLeftSymbol.asTerm.typeSignature.typeSymbol.asClass
    val pointConstructor = pointClassSymbol.info.decl(universe.termNames.CONSTRUCTOR).asMethod
    val pointClassMirror = mirror.reflectClass(pointClassSymbol)
    val rectangleClassMirror = mirror.reflectClass(rectangleClassSymbol)
    val rectangleConstructorMethod = rectangleClassMirror.reflectConstructor(rectangleConstructor)
    val pointConstructorMethod = pointClassMirror.reflectConstructor(pointConstructor)
    val actualTopLeft = pointConstructorMethod(1, 2).asInstanceOf[Point]
    val actualBottomRight = pointConstructorMethod(3, 4).asInstanceOf[Point]
    val actualRectangle = rectangleConstructorMethod(actualTopLeft, actualBottomRight).asInstanceOf[Rectangle]
    assert(actualRectangle === expectedRectangle)
  }
}
