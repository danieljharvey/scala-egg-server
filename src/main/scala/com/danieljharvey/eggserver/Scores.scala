package com.danieljharvey.eggserver

import scala.util.Try

object Scores {

    type ScoreRow = Map[String,Int]

    type ScoreList = List[ScoreRow]

    val tryToInt = ( s: String ) => Try(s.toInt).toOption

    def getScoresFromString(levelIDString: String) = {
        tryToInt(levelIDString)
            .filter(overZero)
            .flatMap(getScoresForLevelID)
            .map(scoreListToStats)
            .getOrElse("No scores found")
    }

    val overZero = (x: Int) => (x > 0)

    def getScoresForLevelID(levelID: Int) = {
        MySQL.getScoresForLevel(levelID)
    }

    type ScoreStats = Map[String,Int]

    case class ScoreTotals(totalCompleted: Int, rotationsUsed: ScoreStats, score: ScoreStats)

    def scoreDefaults() : ScoreTotals = {
        ScoreTotals(
            0,
            Map(
                "min" -> 0,
                "max" -> 0,
                "average" -> 0
            ),
            Map(
                "min" -> 0,
                "max" -> 0,
                "average" -> 0
            )
        )
    }

    def scoreListToStats(scoreList: ScoreList) : ScoreTotals = {
        ScoreTotals(
            countItems(scoreList),
            Map(
                "min" -> 0,
                "max" -> 0,
                "average" -> 0
            ),
            Map(
                "min" -> 0,
                "max" -> 0,
                "average" -> 0
            )
        )
    }

    def countItems(scoreList: ScoreList) : Int = {
        scoreList.length
    }


    def statMin(nums: List[Int]) = nums.min

}