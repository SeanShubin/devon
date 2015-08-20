package com.seanshubin.devon.core.devon

import com.seanshubin.devon.reflection.SimpleTypeConversion

import scala.reflect.runtime._

object ColorConversion extends SimpleTypeConversion {
  override def className: String = universe.typeTag[Color].tpe.toString

  override def toDynamic(x: Any): String = x.asInstanceOf[Color].name

  override def toStatic(x: String): Any = Color.fromString(x)
}
