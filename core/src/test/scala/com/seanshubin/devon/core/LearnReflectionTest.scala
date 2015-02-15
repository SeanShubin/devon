package com.seanshubin.devon.core

import org.scalatest.FunSuite

import scala.reflect.ClassTag
import scala.reflect.runtime.universe

case class LearningSample(map: Map[Int, String]) {
  //  def this(a: Int, b: String) = this(Map(a -> b))
}

class LearnReflectionTest extends FunSuite {
  test("foo") {
    val sampleWithMap = LearningSample(Map(1 -> "a", 2 -> "b"))
    foo(sampleWithMap)
  }

  def foo[T: universe.TypeTag : ClassTag](sample: T): Unit = {
    val typeTag: universe.TypeTag[T] = universe.typeTag[T]
    val tpe: universe.Type = typeTag.tpe
    println(s"tpe = $tpe")
    bar(sample, tpe)
  }

  def bar(value: Any, tpe: universe.Type): Unit = {
    val constructor: universe.MethodSymbol = tpe.decl(universe.termNames.CONSTRUCTOR).asMethod
    println(s"constructor = $constructor")
    val typeSignature: universe.Type = constructor.typeSignature
    println(s"typeSignature = $typeSignature")
    val paramLists: Seq[Seq[universe.Symbol]] = typeSignature.paramLists
    println(s"paramLists = $paramLists")
    val paramList: Seq[universe.Symbol] = paramLists.head
    println(s"paramList = $paramList")
    val param: universe.TermSymbol = paramList.head.asTerm
    println(s"param = $param")
    val paramName: String = param.name.decodedName.toString
    println(s"paramName = $paramName")
    val paramType: universe.Type = param.info
    println(s"paramType = $paramType")
    val typeSymbol: universe.ClassSymbol = paramType.typeSymbol.asClass
    println(s"typeSymbol = $typeSymbol")
    val baseClassNames = typeSymbol.baseClasses.map(x => x.fullName).mkString(" ")
    println(s"baseClassNames = $baseClassNames")
    val typeArgKey: universe.Type = paramType.typeArgs(0)
    println(s"typeArgKey = $typeArgKey")
    val typeArgValue: universe.Type = paramType.typeArgs(1)
    println(s"typeArgValue = $typeArgValue")
    val typeArgKeyClasses = typeArgKey.baseClasses.map(x => x.fullName).mkString(" ")
    println(s"typeArgKeyClasses = $typeArgKeyClasses")
    val typeArgValueClasses = typeArgValue.baseClasses.map(x => x.fullName).mkString(" ")
    println(s"typeArgValueClasses = $typeArgValueClasses")
    val toMapMethod: universe.MethodSymbol = paramType.decl(universe.TermName("toMap")).asMethod
    println(s"toMapMethod = $toMapMethod")
    val mirror = universe.runtimeMirror(param.getClass.getClassLoader)
    println(s"mirror = $mirror")
    val getter: universe.MethodSymbol = tpe.decls.filter(x => x.name.decodedName.toString == paramName).head.asMethod
    println(s"getter = $getter")
    val getterTypeSignature = getter.typeSignature.resultType
    println(s"getterTypeSignature = $getterTypeSignature")


    //    val instanceMirror: universe.InstanceMirror = mirror.reflect(value)
    //    println(s"instanceMirror = $instanceMirror")
    //    val fieldMirror: universe.FieldMirror = instanceMirror.reflectField(getter)
    //    println(s"fieldMirror = $fieldMirror")
    //    val fieldValue = fieldMirror.get
    //    println(s"fieldValue = $fieldValue")
  }

  private def isAccessor(symbol: universe.Symbol): Boolean = {
    symbol.isTerm && symbol.asTerm.isGetter
  }

  def whatIs(x: universe.Symbol): String = {
    if (x.isMethod) {
      assert(!x.isModule)
      assert(!x.isType)
      assert(!x.isClass)
      "method"
    } else if (x.isModule) {
      assert(!x.isMethod)
      assert(!x.isType)
      assert(!x.isClass)
      "module"
    } else if (x.isClass) {
      assert(!x.isModule)
      assert(!x.isMethod)
      assert(!x.isTerm)
      "class"
    } else if (x.isTerm) {
      assert(!x.isMethod)
      assert(!x.isModule)
      assert(!x.isType)
      assert(!x.isClass)
      "term"
    } else if (x.isType) {
      assert(!x.isModule)
      assert(!x.isClass)
      assert(!x.isMethod)
      assert(!x.isTerm)
      "type"
    } else {
      assert(!x.isModule)
      assert(!x.isType)
      assert(!x.isClass)
      assert(!x.isMethod)
      assert(!x.isTerm)
      "symbol"
    }
  }
}
