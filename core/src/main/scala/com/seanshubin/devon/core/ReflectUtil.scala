package com.seanshubin.devon.core

import scala.reflect.ClassTag
import scala.reflect.runtime.universe

object ReflectUtil {
  private val primitiveTypeTags: Seq[universe.TypeTag[_]] = Seq(
    universe.TypeTag.Byte,
    universe.TypeTag.Short,
    universe.TypeTag.Char,
    universe.TypeTag.Int,
    universe.TypeTag.Long,
    universe.TypeTag.Float,
    universe.TypeTag.Double,
    universe.TypeTag.Boolean,
    universe.typeTag[java.lang.String],
    universe.typeTag[scala.math.BigInt],
    universe.typeTag[scala.math.BigDecimal]
  )

  private val primitiveTypeFullNames: Seq[String] = primitiveTypeTags.map(_.tpe.typeSymbol.fullName)

  def construct[T: universe.TypeTag](value: Any, theClass: Class[T]): T = {
    val theType = universe.typeTag[T].tpe.typeSymbol
    constructFromType(value, theType).asInstanceOf[T]
  }

  private def constructFromType(value: Any, theType: universe.Symbol): Any = {
    value match {
      case map: Map[_, _] =>
        val fullName = theType.fullName
        if (fullName == "scala.collection.immutable.Map") {
          ???
        } else {
          constructObject(map.asInstanceOf[Map[String, Any]], theType)
        }
      case x: String => stringToType(x, theType)
      case x => throw new RuntimeException(s"Unsupported: value: $value, type: $theType")
    }
  }

  private def constructFromParameter(value: Any, parameterSymbol: universe.Symbol): Any = {
    val theType = parameterSymbol.asTerm.typeSignature.typeSymbol
    value match {
      case map: Map[_, _] =>
        val fullName = theType.fullName
        if (fullName == "scala.collection.immutable.Map") {
          val keyType = parameterSymbol.info.typeArgs(0).typeSymbol
          val valueType = parameterSymbol.info.typeArgs(1).typeSymbol
          val result = constructMap(keyType, valueType, map.asInstanceOf[Map[Any, Any]])
          result
        } else {
          constructObject(map.asInstanceOf[Map[String, Any]], theType)
        }
      case x: String => stringToType(x, theType)
      case x => throw new RuntimeException(s"Unsupported: value: $value, type: $theType")
    }
  }

  private def constructMap(keyType: universe.Symbol, valueType: universe.Symbol, source: Map[Any, Any]): Map[Any, Any] = {
    def constructEntry(sourceEntry: (Any, Any)): (Any, Any) = {
      val (sourceKey, sourceValue) = sourceEntry
      val key = constructFromType(sourceKey, keyType)
      val value = constructFromType(sourceValue, valueType)
      (key, value)
    }
    source.map(constructEntry)
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
      constructFromParameter(value, parameterSymbol)
    }
    val mirror = universe.runtimeMirror(getClass.getClassLoader)
    val classMirror = mirror.reflectClass(theType.info.typeSymbol.asClass)
    val constructorMethod = classMirror.reflectConstructor(constructor)
    constructorMethod(parameters: _*)
  }

  def pullApart[T: universe.TypeTag : ClassTag](value: T): Any = {
    pullApartWithType(value, universe.typeTag[T].tpe)
  }

  private def pullApartWithType(value: Any, tpe: universe.Type): Any = {
    if (isPrimitive(tpe)) {
      value.toString
    } else if (isMap(tpe)) {
      ???
    } else {
      pullApartObject(value, tpe)
    }
  }

  def isMap(theType: universe.Type): Boolean = {
    val result = theType.baseClasses.map(_.fullName).contains("scala.collection.immutable.Map")
    result
  }

  private def pullApartObject(value: Any, theType: universe.Type): Any = {
    val fields = theType.decls.filter(isAccessor).map(x => x.asTerm)
    val mirror = universe.runtimeMirror(value.getClass.getClassLoader)
    val instanceMirror = mirror.reflect(value)
    val tuples = for {
      field <- fields
      fieldName = field.name.decodedName.toString
      fieldMirror = instanceMirror.reflectField(field)
      rawFieldValue = fieldMirror.get
      classSymbol = field.asTerm.typeSignature
      fieldValue = pullApartWithType(rawFieldValue, field.info)
    } yield {
      (fieldName, fieldValue)
    }
    tuples.toMap
  }

  private def isPrimitive(theType: universe.Type): Boolean = {
    val result = primitiveTypeFullNames.contains(theType.typeSymbol.fullName)
    result
  }

  private def isAccessor(symbol: universe.Symbol): Boolean = {
    symbol.isTerm && symbol.asTerm.isGetter
  }

  private def stringToType(x: String, theType: universe.Symbol): Any = {
    if (theType == universe.TypeTag.Byte.tpe.typeSymbol) x.toByte
    else if (theType == universe.TypeTag.Short.tpe.typeSymbol) x.toShort
    else if (theType == universe.TypeTag.Char.tpe.typeSymbol) stringToChar(x)
    else if (theType == universe.TypeTag.Int.tpe.typeSymbol) x.toInt
    else if (theType == universe.TypeTag.Long.tpe.typeSymbol) x.toLong
    else if (theType == universe.TypeTag.Float.tpe.typeSymbol) x.toFloat
    else if (theType == universe.TypeTag.Double.tpe.typeSymbol) x.toDouble
    else if (theType == universe.TypeTag.Boolean.tpe.typeSymbol) x.toBoolean
    else if (theType.fullName == "java.lang.String") x
    else if (theType.fullName == "scala.math.BigInt") BigInt(x)
    else if (theType.fullName == "scala.math.BigDecimal") BigDecimal(x)
    else throw new RuntimeException(s"Unsupported primitive type ${theType.fullName}")
  }

  private def stringToChar(x: String): Char = {
    if (x.size == 1) x.charAt(0)
    else throw new RuntimeException(s"Cannot convert string '$x' to char, expected length 1, got length ${x.size}")
  }
}
