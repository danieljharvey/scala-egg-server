package com.danieljharvey.eggserver

import scala.util.Try

object ScoresLoader {

    type ScoreList = List[Scores]

    type IntList = List[Int]

    val tryToInt = ( s: String ) => Try(s.toInt).toOption

    def getScoresFromString(levelIDString: String) = {
        tryToInt(levelIDString)
            .filter(overZero)
            .flatMap(getScoresForLevelID)
            .map(scoreListToStats)
            .getOrElse(scoreDefaults)
    }

    val overZero = (x: Int) => (x > 0)

    def getScoresForLevelID(levelID: Int) = {
        MySQL.getScoresForLevel(levelID)
    }

    final case class ScoreStats(min: Int, max: Int, average: Int)

    case class ScoreTotals(totalCompleted: Int, rotationsUsed: ScoreStats, score: ScoreStats)

    def scoreDefaults() : ScoreTotals = {
        ScoreTotals(
            0,
            ScoreStats(
                0,
                0,
                0
            ),
            ScoreStats(
                0,
                0,
                0
            )
        )
    }

    def scoreListToStats(scoreList: ScoreList) : ScoreTotals = {
        ScoreTotals(
            countItems(scoreList),
            allStats(scoreList.map(_.rotationsUsed)),
            allStats(scoreList.map(_.score)),
        )
    }

    def allStats(intList: IntList): ScoreStats = {
        return ScoreStats(
            intList.min,
            intList.max,
            mean(intList)
        )
    }

    def countItems(scoreList: ScoreList) : Int = {
        scoreList.length
    }

    def mean(intList: IntList): Int = {
        intList.sum / intList.length
    }

}