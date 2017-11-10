package com.seanshubin.updater.domain

class Runner(pomFileService: PomFileService,
             updater: Updater) extends Runnable {
  override def run(): Unit = {
    val pomFiles = pomFileService.load()
    val pomFilesInNeedOfUpdate = pomFiles.filter(updater.inNeedOfUpdate)
    val updatedPomFiles = pomFilesInNeedOfUpdate.map(updater.update)
    updatedPomFiles.foreach(pomFileService.store)
  }
}
