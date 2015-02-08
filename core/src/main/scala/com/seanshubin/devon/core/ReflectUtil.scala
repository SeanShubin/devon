package com.seanshubin.devon.core

import scala.reflect.runtime.{universe => ru}

object ReflectUtil {
  def create[T: ru.TypeTag](theClass: Class[T], parameters: Map[String, Any]): T = {
    val theType = ru.typeTag[T].tpe
    val ctor = theType.decl(ru.termNames.CONSTRUCTOR).asMethod
    if (ctor.paramLists.size != 1) {
      throw new RuntimeException("Currently only supports exactly one parameter list in primary constructor")
    }
    val parameterList = constructParameterList(ctor.asMethod.paramLists.head, parameters)
    val m = ru.runtimeMirror(getClass.getClassLoader)
    val theClassSymbol = theType.typeSymbol.asClass
    val cm = m.reflectClass(theClassSymbol)
    val ctorm = cm.reflectConstructor(ctor)
    val created = ctorm(parameterList: _*)
    created.asInstanceOf[T]
  }

  def constructParameterList(symbols: Seq[ru.Symbol], values: Map[String, Any]): Seq[Any] = {
    def pluckValue(symbol: ru.Symbol): Any = {
      val name = symbol.name.decodedName.toString
      val value = values(name)
      value
    }
    symbols.map(pluckValue)
  }
}
