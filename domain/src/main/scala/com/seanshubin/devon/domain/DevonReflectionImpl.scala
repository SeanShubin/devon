package com.seanshubin.devon.domain

import com.seanshubin.devon.reflection.Reflection

import scala.collection.immutable.ListMap
import scala.reflect.runtime.universe

class DevonReflectionImpl(reflection: Reflection) extends DevonReflection {
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
      case x: Seq[_] => makeDevonSeq(x)
      case x: Map[_, _] => makeDevonMap(x)
      case null => DevonNull
      case x => throw new RuntimeException(s"Unsupported: $dynamic of type ${dynamic.getClass.getName}")
    }
  }

  private def makeDevonString(x: String): DevonString = DevonString(x)

  private def makeDevonMap(x: Map[_, _]): DevonMap = {
    val entries = for {
      (key, value) <- x
      devonKey = makeDevon(key)
      devonValue = makeDevon(value)
    } yield {
      (devonKey, devonValue)
    }
    DevonMap(ListMap(entries.toSeq: _*))
  }

  private def makeDevonSeq(x: Seq[_]): DevonArray = {
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
    ListMap[Any, Any](entries.toSeq: _*)
  }
}
