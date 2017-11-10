package com.seanshubin.devon.domain

import com.seanshubin.devon.parser.{Assembler, RuleLookup, StringProcessor}
import com.seanshubin.devon.reflection.{Reflection, ReflectionImpl, SimpleTypeConversion}
import com.seanshubin.devon.tokenizer.{Token, TokenMarshaller, TokenMarshallerImpl}

trait DevonMarshallerWiring {
  def stringProcessor: StringProcessor

  def typeConversions: Seq[SimpleTypeConversion]

  lazy val compactFormatter: CompactDevonFormatter = new CompactDevonFormatterImpl(stringProcessor)
  lazy val prettyFormatter: PrettyDevonFormatter = new PrettyDevonFormatterImpl(stringProcessor)
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
  lazy val Default: DevonMarshaller = builder().build()

  def builder(): Builder = Builder(
    theStringProcessor = NoOperationStringProcessor,
    conversions = SimpleTypeConversion.defaultConversions)

  case class Builder(theStringProcessor: StringProcessor,
                     conversions: List[SimpleTypeConversion]) {
    def addConversion(conversion: SimpleTypeConversion) = copy(conversions = conversion :: conversions)

    def withStringProcessor(newStringProcessor: StringProcessor) = copy(theStringProcessor = newStringProcessor)

    def buildWiring(): DevonMarshallerWiring = new DevonMarshallerWiring {
      override lazy val stringProcessor: StringProcessor = theStringProcessor
      override lazy val typeConversions: Seq[SimpleTypeConversion] = conversions
    }

    def build(): DevonMarshaller = buildWiring().devonMarshaller
  }

}
