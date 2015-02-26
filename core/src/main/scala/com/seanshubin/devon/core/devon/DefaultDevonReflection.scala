package com.seanshubin.devon.core.devon

import com.seanshubin.utility.reflection.{ReflectionImpl, SimpleTypeConversion}

class DefaultDevonReflection extends DevonReflectionImpl(new ReflectionImpl(SimpleTypeConversion.defaultConversions))
