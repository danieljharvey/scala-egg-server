package com.danieljharvey.eggserver

import scala.util.Try

object Levels {

    def tryToInt( s: String ) = Try(s.toInt).toOption

    def getLevelFromString (levelIDString: String) : String = {
        tryToInt(levelIDString)
            .filter(overZero)
            .flatMap(getLevelFromID)
            .getOrElse("No level found")
    }

    def overZero = (x: Int) => (x > 0)

    def getLevelFromID (levelID: Int) : Option[String] = {
        MySQL.getLevel(levelID)
    }

}