package com.seanshubin.devon.parserules

import scala.reflect.runtime.universe

trait DevonReflection {
  def fromValue[T: universe.TypeTag](value: T): Devon

  def toValue[T: universe.TypeTag](devon: Devon, theClass: Class[T]): T
}
