package com.danieljharvey.eggserver

import db._

object MySQL {

  import ctx._

  def getLevel (levelID: Int): Option[String] = {
    val select = quote {
      query[Levels].filter(_.levelID == lift(levelID)).map(_.data)
    }
    ctx.run(select).headOption
  }

}