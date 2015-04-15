package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.token.{Token, TokenMarshaller, TokenMarshallerImpl}
import com.seanshubin.devon.core.{Assembler, NoOperationStringProcessor, RuleLookup, StringProcessor}
import com.seanshubin.utility.reflection.{Reflection, ReflectionImpl, SimpleTypeConversion}

trait DevonMarshallerWiring {
  lazy val stringProcessor: StringProcessor = NoOperationStringProcessor
  lazy val compactFormatter: CompactDevonFormatter = new CompactDevonFormatterImpl(stringProcessor)
  lazy val prettyFormatter: PrettyDevonFormatter = new PrettyDevonFormatterImpl(stringProcessor)
  lazy val typeConversions: Map[String, SimpleTypeConversion] = SimpleTypeConversion.defaultConversions
  lazy val reflection: Reflection = new ReflectionImpl(typeConversions)
  lazy val devonReflection: DevonReflection = new DevonReflectionImpl(reflection)
  lazy val ruleLookup: RuleLookup[Token] = new DevonRuleLookup
  lazy val assembler: Assembler[Token, Devon] = new DevonAssembler
  lazy val tokenMarshaller: TokenMarshaller = new TokenMarshallerImpl(stringProcessor)
  lazy val iteratorFactory: DevonIteratorFactory = new DevonIteratorFactoryImpl(
    ruleLookup,
    assembler,
    tokenMarshaller)
  lazy val devonMarshaller: DevonMarshaller = new DevonMarshallerImpl(
    compactFormatter,
    prettyFormatter,
    devonReflection,
    iteratorFactory
  )
}

object DevonMarshallerWiring {
  lazy val Default: DevonMarshaller = new DevonMarshallerWiring {}.devonMarshaller
}
