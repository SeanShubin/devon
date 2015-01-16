package com.seanshubin.devon.prototype

sealed trait Rps {
  def beats(that: Rps): Boolean
}

case object Rock extends Rps {
  override def beats(that: Rps): Boolean = that match {
    case Scissors => true
    case _ => false
  }
}

case object Paper extends Rps {
  override def beats(that: Rps): Boolean = that match {
    case Rock => true
    case _ => false
  }
}

case object Scissors extends Rps {
  override def beats(that: Rps): Boolean = that match {
    case Paper => true
    case _ => false
  }
}
