package com.seanshubin.devon.core.devon

import com.seanshubin.utility.reflection.{ReflectionImpl, SimpleTypeConversion}

class DefaultDevonMarshaller extends DevonMarshallerImpl(
  compactFormatter = new CompactDevonFormatterImpl,
  prettyFormatter = new PrettyDevonFormatterImpl,
  devonReflection = new DevonReflectionImpl(new ReflectionImpl(SimpleTypeConversion.defaultConversions)),
  iteratorFactory = new DefaultDevonIteratorFactory)