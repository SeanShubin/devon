package com.seanshubin.updater.console

object EntryPoint extends App {
  new DependencyInjection {}.runner.run()
}
