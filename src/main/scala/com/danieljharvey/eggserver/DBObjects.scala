package com.danieljharvey.eggserver

case class Levels(levelID: Int, data: String)

case class Scores(scoreID: Int, levelID: Int, rotationsUsed: Int, score: Int )