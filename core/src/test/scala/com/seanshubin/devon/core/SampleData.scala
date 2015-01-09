package com.seanshubin.devon.core

object SampleData {
  val complexSample =
    """    {
      |      a {b c}
      |      [
      |        d
      |        [e f]
      |      ]
      |      ()
      |      {
      |        ()
      |        [
      |          f
      |          {g h}
      |          ()
      |        ]
      |        {} i
      |      }
      |      j
      |    }
      |    [
      |      [k l]
      |      []
      |      m
      |    ]
      |    n
      |    ()
      |""".stripMargin

}
