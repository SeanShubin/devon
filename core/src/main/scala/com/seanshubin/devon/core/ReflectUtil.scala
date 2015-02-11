package com.seanshubin.devon.core

import scala.reflect.ClassTag
import scala.reflect.runtime.universe

object ReflectUtil {
  def construct[T: universe.TypeTag](value: Any, theClass: Class[T]): T = {
    val theType = universe.typeTag[T].tpe.typeSymbol
    constructFromType(value, theType).asInstanceOf[T]
  }

  private def constructFromType(value: Any, theType: universe.Symbol): Any = {
    value match {
      case map: Map[_, _] => constructObject(map.asInstanceOf[Map[String, Any]], theType)
      case x:String => stringToType(x, theType)
      case x => throw new RuntimeException(s"Unsupported: value: $value, type: $theType")
    }
  }

  private def constructObject(map: Map[String, Any], theType: universe.Symbol): Any = {
    val constructor: universe.MethodSymbol = theType.info.decl(universe.termNames.CONSTRUCTOR).asMethod
    if (constructor.typeSignature.paramLists.size != 1) {
      throw new RuntimeException("multiple parameter lists not supported")
    }
    val parameterSymbols: Seq[universe.Symbol] = constructor.typeSignature.paramLists.head
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
    constructorMethod(parameters: _*)
  }

  def pullApart[T: universe.TypeTag : ClassTag](value: T): Any = {
    pullApartWithType(value, universe.typeTag[T].tpe.typeSymbol)
  }

  private def pullApartWithType(value: Any, theType: universe.Symbol): Any = {
    if (isPrimitive(theType)) {
      value.toString
    } else {
      pullApartObject(value, theType)
    }
  }

  private def pullApartObject(value: Any, theType: universe.Symbol): Any = {
    val fields = theType.info.decls.filter(isAccessor).map(x => x.asTerm)
    val mirror = universe.runtimeMirror(value.getClass.getClassLoader)
    val instanceMirror = mirror.reflect(value)
    val tuples = for {
      field <- fields
      fieldName = field.name.decodedName.toString
      fieldMirror = instanceMirror.reflectField(field)
      rawFieldValue = fieldMirror.get
      classSymbol = field.asTerm.typeSignature.typeSymbol
      fieldValue = pullApartWithType(rawFieldValue, classSymbol)
    } yield {
      (fieldName, fieldValue)
    }
    tuples.toMap
  }

  private def isPrimitive(theType: universe.Symbol): Boolean = {
    theType == universe.TypeTag.Int.tpe.typeSymbol
  }

  private def isAccessor(symbol: universe.Symbol): Boolean = {
    symbol.isTerm && symbol.asTerm.isGetter
  }

  private def stringToType(x:String, theType: universe.Symbol):Any = {
    if(theType == universe.TypeTag.Int.tpe.typeSymbol) x.toInt
    else throw new RuntimeException(s"Unsupported primitive type $theType")
  }
}
