package com.seanshubin.devon.reflection

import java.io.File
import java.nio.file.{Path, Paths}
import java.time.{Instant, ZonedDateTime}

import scala.reflect.runtime._

trait SimpleTypeConversion {
  def className: String

  def toDynamic(x: Any): String

  def toStatic(x: String): Any
}

object SimpleTypeConversion {

  object ByteConversion extends SimpleTypeConversion {
    override def className: String = universe.TypeTag.Byte.tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Byte].toString

    override def toStatic(x: String): Any = x.toByte
  }

  object ShortConversion extends SimpleTypeConversion {
    override def className: String = universe.TypeTag.Short.tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Short].toString

    override def toStatic(x: String): Any = x.toShort
  }

  object CharConversion extends SimpleTypeConversion {
    override def className: String = universe.TypeTag.Char.tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Char].toString

    override def toStatic(x: String): Any = x.asInstanceOf[String].charAt(0)
  }

  object IntConversion extends SimpleTypeConversion {
    override def className: String = universe.TypeTag.Int.tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Int].toString

    override def toStatic(x: String): Any = x.toInt
  }

  object LongConversion extends SimpleTypeConversion {
    override def className: String = universe.TypeTag.Long.tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Long].toString

    override def toStatic(x: String): Any = x.toLong
  }

  object FloatConversion extends SimpleTypeConversion {
    override def className: String = universe.TypeTag.Float.tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Float].toString

    override def toStatic(x: String): Any = x.toFloat
  }

  object DoubleConversion extends SimpleTypeConversion {
    override def className: String = universe.TypeTag.Double.tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Double].toString

    override def toStatic(x: String): Any = x.toDouble
  }

  object BooleanConversion extends SimpleTypeConversion {
    override def className: String = universe.TypeTag.Boolean.tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Boolean].toString

    override def toStatic(x: String): Any = x.toBoolean
  }

  object UnitConversion extends SimpleTypeConversion {
    override def className: String = universe.TypeTag.Unit.tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Unit].toString

    override def toStatic(x: String): Any = ()
  }

  object NullConversion extends SimpleTypeConversion {
    override def className: String = universe.TypeTag.Null.tpe.toString

    override def toDynamic(x: Any): String = "" + x.asInstanceOf[Null]

    override def toStatic(x: String): Any = null
  }

  object ScalaStringConversion extends SimpleTypeConversion {
    override def className: String = universe.typeTag[String].tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[String]

    override def toStatic(x: String): Any = x
  }

  object JavaStringConversion extends SimpleTypeConversion {
    override def className: String = "java.lang.String"

    override def toDynamic(x: Any): String = x.asInstanceOf[String]

    override def toStatic(x: String): Any = x
  }

  object BigIntConversion extends SimpleTypeConversion {
    override def className: String = universe.typeTag[BigInt].tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[BigInt].toString()

    override def toStatic(x: String): Any = BigInt(x)
  }

  object BigDecimalConversion extends SimpleTypeConversion {
    override def className: String = universe.typeTag[BigDecimal].tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[BigDecimal].toString()

    override def toStatic(x: String): Any = BigDecimal(x)
  }

  object ZonedDateTimeConversion extends SimpleTypeConversion {
    override def className: String = universe.typeTag[ZonedDateTime].tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[ZonedDateTime].toString

    override def toStatic(x: String): Any = ZonedDateTime.parse(x)
  }

  object InstantConversion extends SimpleTypeConversion {
    override def className: String = universe.typeTag[Instant].tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Instant].toString

    override def toStatic(x: String): Any = Instant.parse(x)
  }

  object PathConversion extends SimpleTypeConversion {
    override def className: String = universe.typeTag[Path].tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[Path].toString

    override def toStatic(x: String): Any = Paths.get(x)
  }

  object FileConversion extends SimpleTypeConversion {
    override def className: String = universe.typeTag[File].tpe.toString

    override def toDynamic(x: Any): String = x.asInstanceOf[File].toString

    override def toStatic(x: String): Any = new File(x)
  }

  val defaultConversions: List[SimpleTypeConversion] = List(
    ByteConversion,
    ShortConversion,
    CharConversion,
    IntConversion,
    LongConversion,
    FloatConversion,
    DoubleConversion,
    BooleanConversion,
    UnitConversion,
    NullConversion,
    ScalaStringConversion,
    JavaStringConversion,
    BigIntConversion,
    BigDecimalConversion,
    ZonedDateTimeConversion,
    InstantConversion,
    PathConversion,
    FileConversion
  )

  def toMapEntry(conversion: SimpleTypeConversion): (String, SimpleTypeConversion) = conversion.className -> conversion

  def toMap(conversions: Seq[SimpleTypeConversion]): Map[String, SimpleTypeConversion] = conversions.map(toMapEntry).toMap
}
