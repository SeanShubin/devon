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
      case x: Int => x
      case _ =>
        throw new RuntimeException(s"Unsupported: value $value, type $theType")
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
    val m = universe.runtimeMirror(value.getClass.getClassLoader)
    val theType = universe.typeTag[T].tpe
    val fields = theType.decls.filter(isAccessor).map(x => x.asTerm)
    val im = m.reflect(value)
    def pluckValue(accessor: universe.TermSymbol): (String, Any) = {
      val fieldMirror = im.reflectField(accessor)
      val fieldName = accessor.name.decodedName.toString
      val fieldValue = fieldMirror.get
      (fieldName, fieldValue)
    }
    fields.map(pluckValue).toMap
  }

  private def isAccessor(symbol: universe.Symbol): Boolean = {
    symbol.isTerm && symbol.asTerm.isGetter
  }
}
