package com.seanshubin.updater.domain

trait Updater {
  def inNeedOfUpdate(pomFile: PomFile): Boolean

  def update(pomFile: PomFile): PomFile
}
