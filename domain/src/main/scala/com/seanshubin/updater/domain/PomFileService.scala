package com.seanshubin.updater.domain

trait PomFileService {
  def load(): Seq[PomFile]

  def store(pomFile: PomFile): Unit
}
