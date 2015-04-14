package com.seanshubin.devon.core.devon

import com.seanshubin.devon.core.NoOperationStringProcessor
import com.seanshubin.utility.reflection.{ReflectionImpl, SimpleTypeConversion}

object DefaultDevonMarshaller extends DevonMarshallerImpl(
  compactFormatter = new CompactDevonFormatterImpl(NoOperationStringProcessor),
  prettyFormatter = new PrettyDevonFormatterImpl(NoOperationStringProcessor),
  devonReflection = new DevonReflectionImpl(
    new ReflectionImpl(SimpleTypeConversion.defaultConversions)),
  iteratorFactory = new DefaultDevonIteratorFactory)
