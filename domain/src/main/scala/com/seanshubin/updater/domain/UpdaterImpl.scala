package com.seanshubin.updater.domain

class UpdaterImpl extends Updater {
  override def inNeedOfUpdate(pomFile: PomFile): Boolean = ???

  override def update(pomFile: PomFile): PomFile = ???
}
