package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.token.TokenMarshallerImpl

class DefaultDevonIteratorFactory extends DevonIteratorFactoryImpl(
  ruleLookup = new DevonRuleLookup,
  assembler = new DevonAssembler,
  tokenMarshaller = new TokenMarshallerImpl)
