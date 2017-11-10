package com.seanshubin.updater.console

import com.seanshubin.updater.domain._

trait DependencyInjection {
  val pomFileService: PomFileService = new PomFileServiceImpl()
  val updater: Updater = new UpdaterImpl()
  val runner: Runnable = new Runner(pomFileService, updater)
}
