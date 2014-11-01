package com.seanshubin.devon.console

import com.seanshubin.devon.core.{NotationImpl, Notation}

object ConsoleApplication extends App {
  val text = "{a{b c}[d [e f]](){()[f{g h}()]{}i}j}[[k l][]m]n()"
  val notation:Notation = new NotationImpl()
  val pretty = notation.pretty(text)
  println(pretty)
}
