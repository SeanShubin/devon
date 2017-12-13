package com.seanshubin.devon.reflection

import scala.collection.immutable.ListMap
import scala.reflect.runtime._

class ReflectionImpl(simpleTypeConversionsSeq: Seq[SimpleTypeConversion]) extends Reflection {
  private val simpleTypeConversions: Map[String, SimpleTypeConversion] = SimpleTypeConversion.toMap(simpleTypeConversionsSeq)
  private val mirror: universe.Mirror = universe.runtimeMirror(getClass.getClassLoader)
  private val TupleNameRegex = """scala\.Tuple\d+""".r

  override def pieceTogether[T: universe.TypeTag](dynamicValue: Any, staticClass: Class[T]): T = {
    val tpe: universe.Type = universe.typeOf[T]
    val result = pieceTogetherAny(dynamicValue, tpe).asInstanceOf[T]
    result
  }

  override def pullApart[T: universe.TypeTag](staticValue: T): Any = {
    val tpe = universe.typeTag[T].tpe
    val dynamicValue = pullApartAny(staticValue, tpe)
    dynamicValue
  }

  private def pieceTogetherAny(dynamicValue: Any, tpe: universe.Type): Any = {
    val result = simpleTypeConversions.get(tpe.toString) match {
      case Some(simpleTypeConversion) => simpleTypeConversion.toStatic(dynamicValue.asInstanceOf[String])
      case None => createComplex(tpe).pieceTogetherAny(dynamicValue, tpe)
    }
    result
  }

  private def pieceTogetherCaseClass(valueMap: Map[String, Any], tpe: universe.Type): Any = {
    val constructor: universe.MethodSymbol = tpe.decl(universe.termNames.CONSTRUCTOR).asMethod
    val constructorParameters: Seq[universe.TermSymbol] = constructor.typeSignature.paramLists.head.map(_.asTerm)
    val parameterList: Seq[Any] = createParameterListFromMap(constructorParameters, valueMap)
    val typeClass: universe.ClassSymbol = tpe.typeSymbol.asClass
    val classMirror: universe.ClassMirror = mirror.reflectClass(typeClass)
    val constructorMethod: universe.MethodMirror = classMirror.reflectConstructor(constructor)
    val constructed: Any = constructorMethod(parameterList: _*)
    constructed
  }

  private def pieceTogetherTuple(valueSeq: Seq[Any], tpe: universe.Type): Any = {
    val constructor: universe.MethodSymbol = tpe.decl(universe.termNames.CONSTRUCTOR).asMethod
    val parameterList: Seq[Any] = createParameterListFromSeq(tpe.typeArgs, valueSeq)
    val typeClass: universe.ClassSymbol = tpe.typeSymbol.asClass
    val classMirror: universe.ClassMirror = mirror.reflectClass(typeClass)
    val constructorMethod: universe.MethodMirror = classMirror.reflectConstructor(constructor)
    val constructed: Any = constructorMethod(parameterList: _*)
    constructed
  }

  private def pieceTogetherOption(maybeValue: Any, tpe: universe.Type): Any = {
    val optionType: universe.Type = tpe.typeArgs.head
    val optionContents = pieceTogetherAny(maybeValue, optionType)
    val optionValue = Option(optionContents)
    optionValue
  }

  private def pieceTogetherSeq(dynamicSeq: Seq[Any], tpe: universe.Type): Seq[Any] = {
    val elementType: universe.Type = tpe.typeArgs.head
    def pieceTogetherElement(dynamicValue: Any): Any = {
      pieceTogetherAny(dynamicValue, elementType)
    }
    val staticSeq = dynamicSeq.map(pieceTogetherElement)
    staticSeq
  }

  private def pieceTogetherMap(dynamicMap: Map[Any, Any], tpe: universe.Type): Map[Any, Any] = {
    val keyElementType: universe.Type = tpe.typeArgs(0)
    val valueElementType: universe.Type = tpe.typeArgs(1)
    def pieceTogetherEntry(dynamicEntry: (Any, Any)): (Any, Any) = {
      val (dynamicKey, dynamicValue) = dynamicEntry
      val staticKey = pieceTogetherAny(dynamicKey, keyElementType)
      val staticValue = pieceTogetherAny(dynamicValue, valueElementType)
      (staticKey, staticValue)
    }

    val staticMap = dynamicMap match {
      case x: ListMap[Any, Any] =>
        // preserve order if we are a list map
        x.map(pieceTogetherEntry)
      case _ => dynamicMap.map(pieceTogetherEntry)
    }
    staticMap
  }

  private def createParameterListFromMap(constructorParameters: Seq[universe.TermSymbol], valueMap: Map[String, Any]): Seq[Any] = {
    def lookupValue(term: universe.TermSymbol): Any = {
      val parameterName = symbolName(term)
      val parameterType: universe.Type = term.info
      if (isPrimitive(parameterType) && !valueMap.contains(parameterName)) {
        throw new RuntimeException(s"Missing value for $parameterName of type $parameterType")
      }
      val parameterValue = valueMap.get(parameterName) match {
        case Some(dynamicParameterValue) =>
          pieceTogetherAny(dynamicParameterValue, parameterType)
        case None =>
          if (isOption(parameterType)) None
          else null
      }
      parameterValue
    }
    val parameterList = constructorParameters.map(lookupValue)
    parameterList
  }

  private def createParameterListFromSeq(constructorParameters: Seq[universe.Type], valueSeq: Seq[Any]): Seq[Any] = {
    def lookupValue(termAndValue: (universe.Type, Any)): Any = {
      val (parameterType, value) = termAndValue
      val parameterValue = pieceTogetherAny(value, parameterType)
      parameterValue
    }
    val termsAndValues = constructorParameters zip valueSeq
    val parameterList = termsAndValues.map(lookupValue)
    parameterList
  }

  private def isPrimitive(tpe: universe.Type): Boolean = {
    tpe.baseClasses.map(_.fullName).contains("scala.AnyVal")
  }

  private def isOption(tpe: universe.Type): Boolean = {
    tpe.baseClasses.map(_.fullName).contains("scala.Option")
  }

  private def symbolName(parameter: universe.Symbol): String = parameter.name.decodedName.toString

  private def pullApartAny(staticValue: Any, tpe: universe.Type): Any = {
    val maybeSimpleTypeConversion = simpleTypeConversions.get(tpe.toString)
    val result = maybeSimpleTypeConversion match {
      case Some(simpleTypeConversion) =>
        if (staticValue == null) {
          null
        } else {
          simpleTypeConversion.toDynamic(staticValue)
        }
      case None => createComplex(tpe).pullApartAny(staticValue, tpe)
    }
    result
  }

  private def pullApartCaseClass(value: Any, tpe: universe.Type): ListMap[String, Any] = {
    val fields: Iterable[universe.TermSymbol] = tpe.decls.map(_.asTerm).filter(_.isGetter)
    val instanceMirror: universe.InstanceMirror = mirror.reflect(value)
    def createEntry(field: universe.TermSymbol): (String, Any) = {
      val fieldName = symbolName(field)
      val fieldMirror: universe.FieldMirror = instanceMirror.reflectField(field)
      val staticFieldValue = fieldMirror.get
      val fieldType = field.typeSignature.resultType
      val dynamicFieldValue = pullApartAny(staticFieldValue, fieldType)
      val entry = (fieldName, dynamicFieldValue)
      entry
    }
    val entries: Iterable[(String, Any)] = fields.map(createEntry)
    val map: ListMap[String, Any] = ListMap(entries.toSeq: _*)
    map
  }

  private def pullApartTuple(value: Any, tpe: universe.Type): Seq[Any] = {
    val fields: Iterable[universe.TermSymbol] = tpe.decls.map(_.asTerm).filter(_.isGetter)
    val types = tpe.typeArgs
    val instanceMirror: universe.InstanceMirror = mirror.reflect(value)
    def createEntry(fieldAndType: (universe.TermSymbol, universe.Type)): Any = {
      val (field, theType) = fieldAndType
      val fieldMirror: universe.FieldMirror = instanceMirror.reflectField(field)
      val staticFieldValue = fieldMirror.get
      val dynamicFieldValue = pullApartAny(staticFieldValue, theType)
      dynamicFieldValue
    }
    val fieldsAndTypes = fields zip types
    val entries: Iterable[Any] = fieldsAndTypes.map(createEntry)
    entries.toSeq
  }

  private def pullApartOption(value: Option[Any], tpe: universe.Type): Any = {
    val optionContents = value match {
      case Some(x) =>
        val elementType: universe.Type = tpe.typeArgs.head
        pullApartAny(x, elementType)
      case None =>
        null
    }
    optionContents
  }

  private def pullApartSeq(staticSeq: Seq[Any], tpe: universe.Type): Seq[Any] = {
    val elementType: universe.Type = tpe.typeArgs.head
    def pullApartElement(element: Any): Any = {
      pullApartAny(element, elementType)
    }
    val dynamicSeq = staticSeq.map(pullApartElement)
    dynamicSeq
  }

  private def pullApartMap(staticMap: Map[Any, Any], tpe: universe.Type): Map[Any, Any] = {
    val keyElementType: universe.Type = tpe.typeArgs(0)
    val valueElementType: universe.Type = tpe.typeArgs(1)
    def pullApartEntry(staticEntry: (Any, Any)): (Any, Any) = {
      val (staticKey, staticValue) = staticEntry
      val dynamicKey = pullApartAny(staticKey, keyElementType)
      val dynamicValue = pullApartAny(staticValue, valueElementType)
      (dynamicKey, dynamicValue)
    }

    // in case we have an ordered map, make sure that order is preserved
    val dynamicMap = ListMap(staticMap.toSeq.map(pullApartEntry): _*)
    dynamicMap
  }

  private def createComplex(theType: universe.Type): Complex = {
    val fullNames = theType.baseClasses.map(_.fullName)
    val result = if (fullNames.contains("scala.Option")) ComplexOption
    else if (fullNames.contains("scala.collection.immutable.Map")) ComplexMap
    else if (fullNames.contains("scala.collection.Seq")) ComplexSeq
    else if (fullNames.contains("scala.collection.Set")) ComplexSet
    else if (fullNames.exists(isTupleName)) ComplexTuple
    else if (fullNames.contains("scala.Product")) ComplexCaseClass
    else throw new RuntimeException(s"Unsupported type: $theType")
    result
  }

  private def isTupleName(name: String): Boolean = {
    TupleNameRegex.pattern.matcher(name).matches()
  }

  private sealed trait Complex {
    def pullApartAny(staticValue: Any, tpe: universe.Type): Any

    def pieceTogetherAny(dynamicValue: Any, tpe: universe.Type): Any
  }

  private object ComplexOption extends Complex {
    override def pullApartAny(staticValue: Any, tpe: universe.Type): Any =
      pullApartOption(staticValue.asInstanceOf[Option[Any]], tpe)

    override def pieceTogetherAny(dynamicValue: Any, tpe: universe.Type): Any =
      pieceTogetherOption(dynamicValue, tpe)
  }

  private object ComplexCaseClass extends Complex {
    override def pullApartAny(staticValue: Any, tpe: universe.Type): Any =
      if (staticValue == null) {
        null
      } else {
        pullApartCaseClass(staticValue, tpe)
      }

    override def pieceTogetherAny(dynamicValue: Any, tpe: universe.Type): Any =
      if (dynamicValue == null) {
        null
      } else {
        pieceTogetherCaseClass(dynamicValue.asInstanceOf[Map[String, Any]], tpe)
      }
  }

  private object ComplexTuple extends Complex {
    override def pullApartAny(staticValue: Any, tpe: universe.Type): Any =
      if (staticValue == null) {
        null
      } else {
        pullApartTuple(staticValue, tpe)
      }

    override def pieceTogetherAny(dynamicValue: Any, tpe: universe.Type): Any =
      if (dynamicValue == null) {
        null
      } else {
        pieceTogetherTuple(dynamicValue.asInstanceOf[Seq[String]], tpe)
      }
  }

  private object ComplexSeq extends Complex {
    override def pullApartAny(staticValue: Any, tpe: universe.Type): Any = {
      if (staticValue == null) {
        null
      } else {
        pullApartSeq(staticValue.asInstanceOf[Seq[Any]], tpe)
      }
    }

    override def pieceTogetherAny(dynamicValue: Any, tpe: universe.Type): Any =
      if (dynamicValue == null) {
        null
      } else {
        pieceTogetherSeq(dynamicValue.asInstanceOf[Seq[Any]], tpe)
      }
  }

  private object ComplexSet extends Complex {
    override def pullApartAny(staticValue: Any, tpe: universe.Type): Any =
      if (staticValue == null) {
        null
      } else {
        pullApartSeq(staticValue.asInstanceOf[Set[Any]].toSeq, tpe)
      }

    override def pieceTogetherAny(dynamicValue: Any, tpe: universe.Type): Any =
      if (dynamicValue == null) {
        null
      } else {
        pieceTogetherSeq(dynamicValue.asInstanceOf[Seq[Any]], tpe).toSet
      }
  }

  private object ComplexMap extends Complex {
    override def pullApartAny(staticValue: Any, tpe: universe.Type): Any =
      if (staticValue == null) {
        null
      } else {
        pullApartMap(staticValue.asInstanceOf[Map[Any, Any]], tpe)
      }

    override def pieceTogetherAny(dynamicValue: Any, tpe: universe.Type): Any =
      if (dynamicValue == null) {
        null
      } else {
        pieceTogetherMap(dynamicValue.asInstanceOf[Map[Any, Any]], tpe)
      }
  }

}
