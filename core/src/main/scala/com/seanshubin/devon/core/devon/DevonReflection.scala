package com.seanshubin.devon.core.devon

import com.seanshubin.utility.reflection.Reflection

import scala.reflect.runtime.universe

class DevonReflection(reflection: Reflection) {
  def fromValue[T: universe.TypeTag](value: T): Devon = {
    val dynamic = reflection.pullApart(value)
    makeDevon(dynamic)
  }

  def toValue[T: universe.TypeTag](devon: Devon, theClass: Class[T]) = {
    val dynamic = makeDynamic(devon)
    val static = reflection.pieceTogether(dynamic, theClass)
    static
  }

  private def makeDevon(dynamic: Any): Devon = {
    dynamic match {
      case x: String => makeDevonString(x)
      case x: Seq[Any] => makeDevonSeq(x)
      case x: Map[Any, Any] => makeDevonMap(x)
      case null => DevonNull
      case x => throw new RuntimeException(s"Unsupported: $dynamic of type ${dynamic.getClass.getName}")
    }
  }

  private def makeDevonString(x: String): DevonString = DevonString(x)

  private def makeDevonMap(x: Map[Any, Any]): DevonMap = {
    val entries = for {
      (key, value) <- x
      devonKey = makeDevon(key)
      devonValue = makeDevon(value)
    } yield {
      (devonKey, devonValue)
    }
    DevonMap(entries.toMap)
  }

  private def makeDevonSeq(x: Seq[Any]): DevonArray = {
    val devonElements = x.map(makeDevon)
    DevonArray(devonElements)
  }

  private def makeDynamic(devon: Devon): Any = {
    devon match {
      case x: DevonString => makeDynamicString(x)
      case x: DevonArray => makeDynamicArray(x)
      case x: DevonMap => makeDynamicMap(x)
      case DevonNull => null
      case null => throw new RuntimeException(s"Unsupported: null")
      case x => throw new RuntimeException(s"Unsupported: $devon of type ${devon.getClass.getName}")
    }
  }

  private def makeDynamicString(x: DevonString): String = x.string

  private def makeDynamicArray(x: DevonArray): Seq[Any] = x.array.map(makeDynamic)

  private def makeDynamicMap(x: DevonMap): Map[Any, Any] = {
    val entries = for {
      (key, value) <- x.map
      dynamicKey = makeDynamic(key)
      dynamicValue = makeDynamic(value)
    } yield {
      (dynamicKey, dynamicValue)
    }
    entries.toMap
  }
}
