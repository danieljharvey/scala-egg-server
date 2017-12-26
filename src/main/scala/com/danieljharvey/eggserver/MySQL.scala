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

  def getScoresForLevel (levelID: Int): Option[List[Scores]] = {
    val select = quote {
      query[Scores].filter(_.levelID == lift(levelID))
    }
    emptyOrNone(ctx.run(select))
  }

  def emptyOrNone (scores: List[Scores]): Option[List[Scores]] = {
    scores match {
      case Nil => None
      case x => Some(x)
    }
  }

}