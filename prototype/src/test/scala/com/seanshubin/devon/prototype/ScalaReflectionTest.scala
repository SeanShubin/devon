package com.seanshubin.devon.prototype

import org.scalatest.FunSuite

import scala.reflect.runtime._
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
    val rectangleClassSymbol: universe.ClassSymbol = rectangleTypeTag.tpe.typeSymbol.asClass
    val pointClassSymbol: universe.ClassSymbol = topLeftSymbol.asTerm.typeSignature.typeSymbol.asClass
    val pointConstructor = pointClassSymbol.info.decl(universe.termNames.CONSTRUCTOR).asMethod
    val pointClassMirror = mirror.reflectClass(pointClassSymbol)
    val rectangleClassMirror = mirror.reflectClass(rectangleClassSymbol)
    val rectangleConstructorMethod = rectangleClassMirror.reflectConstructor(rectangleConstructor)
    val pointConstructorMethod = pointClassMirror.reflectConstructor(pointConstructor)
    val actualTopLeft = pointConstructorMethod(1, 2).asInstanceOf[Point]
    val xSymbol = pointConstructor.typeSignature.paramLists.head.head
    val xClassSymbol = xSymbol.asTerm.typeSignature.typeSymbol.asClass
    assert(xClassSymbol === universe.typeTag[Int].tpe.typeSymbol)

    val actualBottomRight = pointConstructorMethod(3, 4).asInstanceOf[Point]
    val actualRectangle = rectangleConstructorMethod(actualTopLeft, actualBottomRight).asInstanceOf[Rectangle]
    assert(actualRectangle === expectedRectangle)

    val values = Map("topLeft" -> Map("x" -> 1, "y" -> 2), "bottomRight" -> Map("x" -> 3, "y" -> 4))
    println(construct(values, classOf[Rectangle]))
  }

  def construct[T:universe.TypeTag](value:Any, theClass:Class[T]):T = {
    val theType = universe.typeTag[T].tpe.typeSymbol
    constructFromType(value, theType).asInstanceOf[T]
  }

  def constructFromType(value:Any, theType:universe.Symbol):Any = {
    value match {
      case map:Map[_,_] => constructObject(map.asInstanceOf[Map[String, Any]], theType)
      case x:Int => x
      case _ =>
        throw new RuntimeException(s"Unsupported: value $value, type $theType")
    }
  }

  private def constructObject(map:Map[String, Any], theType:universe.Symbol):Any = {
    val constructor:universe.MethodSymbol = theType.info.decl(universe.termNames.CONSTRUCTOR).asMethod
    if(constructor.typeSignature.paramLists.size != 1) {
      throw new RuntimeException("multiple parameter lists not supported")
    }
    val parameterSymbols:Seq[universe.Symbol] = constructor.typeSignature.paramLists.head
    val parameters = for {
      parameterSymbol <- parameterSymbols
      key = parameterSymbol.name.decodedName.toString
      value = map(key)
      classSymbol = parameterSymbol.asTerm.typeSignature.typeSymbol
    } yield {
      constructFromType(value, classSymbol)
    }
    val mirror = universe.runtimeMirror(getClass.getClassLoader)
    val classMirror = mirror.reflectClass(theType.info.typeSymbol.asClass)
    val constructorMethod = classMirror.reflectConstructor(constructor)
    constructorMethod(parameters:_*)
  }
}
