package com.danieljharvey.eggserver

import scala.util.Try
import net.liftweb.json._
import net.liftweb.json.Serialization.write

case class BoardSize (width: Int, height: Int)

// type that json will turn into
case class JSONTile (
    action: Option[String],
    background: Option[Boolean],
    breakable: Option[Boolean],
    collectable: Option[Int],
    createPlayer: Option[String],
    dontAdd: Option[Boolean],
    frontLayer: Option[Boolean],
    id: Option[Int],
    title: Option[String],
    x: Option[String],
    y: Option[String]
)

// actual nice type with no options and defaults instead
case class Tile (
    action: String = "",
    background: Boolean = false,
    breakable: Boolean = false,
    collectable: Int = 0,
    createPlayer: String = "",
    dontAdd: Boolean = false,
    frontLayer: Boolean = false,
    id: Int = 0,
    title: String = "Title",
    x: Int = 0,
    y: Int = 0
)

object LevelsLoader {

    case class JSONLevelData (board: JSONBoard, boardSize: BoardSize, levelID: String)

    case class LevelData (board: Board, boardSize: BoardSize, levelID: Int, scores: ScoresLoader.ScoreTotals)

    type JSONBoard = List[List[JSONTile]]

    type Board = List[List[Tile]]

    def tryToInt( s: String ) = Try(s.toInt).toOption

    def getLevelFromString (levelIDString: String) : Option[String] = {
        tryToInt(levelIDString)
            .filter(overZero)
            .flatMap(getLevelFromID)
            .map(decodeLevelData)
            .map(tidyLevel)
            .map(toJSON)
    }

    def overZero = (x: Int) => (x > 0)

    def getLevelFromID (levelID: Int) : Option[String] = {
        MySQL.getLevel(levelID)
    }

    def decodeLevelData (json: String): JSONLevelData = {
        implicit val formats = DefaultFormats
        parse(json).extract[JSONLevelData]
    }

    def tidyLevel (levelData: JSONLevelData): LevelData = {
        val newBoard = levelData.board.map(row => row.map(jsonTile => {
            tidyTile(jsonTile)
        }))
        LevelData(
            board = newBoard,
            boardSize = levelData.boardSize,
            levelID = tryToInt(levelData.levelID).getOrElse(0),
            scores = ScoresLoader.getScoresFromString(levelData.levelID)
        )
    } 

    def tidyTile (jsonTile: JSONTile): Tile = {
        new Tile(
            action = jsonTile.action.getOrElse(""),
            background = jsonTile.background.getOrElse(false),
            breakable = jsonTile.background.getOrElse(false),
            collectable = jsonTile.collectable.getOrElse(0),
            createPlayer = jsonTile.createPlayer.getOrElse(""),
            dontAdd = jsonTile.dontAdd.getOrElse(false),
            frontLayer = jsonTile.frontLayer.getOrElse(false),
            id = jsonTile.id.getOrElse(0),
            title = jsonTile.title.getOrElse("Title"),
            x = jsonTile.x.flatMap(tryToInt).getOrElse(0),
            y = jsonTile.y.flatMap(tryToInt).getOrElse(0)
        )
    }

    def toJSON (levelData: LevelData): String = {
        implicit val formats = DefaultFormats
        write(levelData)
    }

}