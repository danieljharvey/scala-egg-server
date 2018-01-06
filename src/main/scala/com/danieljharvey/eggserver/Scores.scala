package com.danieljharvey.eggserver

import scala.util.Try

import net.liftweb.json._
import net.liftweb.json.Serialization.write

object ScoresLoader {

    case class ScoreStats(min: Int, max: Int, average: Int)

    case class ScoreTotals(totalCompleted: Int, rotationsUsed: ScoreStats, score: ScoreStats)

    case class ScoreRow(rotationsUsed: Int, score: Int)

    case class SaveScoreRow(levelID: Int, rotationsUsed: Int, score: Int)

    type ScoreList = List[Scores]

    type IntList = List[Int]

    val tryToInt = ( s: String ) => Try(s.toInt).toOption

    def getScoresFromString(levelIDString: String): ScoreTotals = {
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

    def jsonStringify(scoreTotals: ScoreTotals): String = {
        implicit val formats = DefaultFormats
        write(scoreTotals)
    }

    def saveScoresForLevelString(levelIDString: String, body: String) = {
        
        val levelID = tryToInt(levelIDString)

        tryToDecodeJSON(body)
            .flatMap(combineWithLevelID(levelID))
            .map(MySQL.saveScoresForLevel)
            .map(println)
    }

    def tryToDecodeJSON = ( json: String ) => Try(decodeScoresJSON(json)).toOption

    def decodeScoresJSON(jsonString: String): ScoreRow = {

        // needed for Lift-JSON
        implicit val formats = DefaultFormats

        // convert the JSON string to a JValue object
        val jValue = parse(jsonString)

        // deserialize the string into a Stock object
        jValue.extract[ScoreRow]
    }

    def combineWithLevelID(maybeLevelID: Option[Int])(scoreRow: ScoreRow): Option[SaveScoreRow] = {
        maybeLevelID.map(levelID => 
            SaveScoreRow(levelID=levelID, rotationsUsed=scoreRow.rotationsUsed, score=scoreRow.score)
        )
    }

}