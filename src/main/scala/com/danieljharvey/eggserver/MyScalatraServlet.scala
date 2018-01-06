package com.danieljharvey.eggserver

import org.scalatra._

class MyScalatraServlet extends ScalatraServlet {

  get("/") {
    "Hello welcome to Egg Server"
  }

  get("/levels") {
    <p>Let's show a list of levels</p>
  }

  get("/levels/:levelID") {
    // Matches "GET /hello/foo" and "GET /hello/bar"
    // params("name") is "foo" or "bar"
    LevelsLoader.getLevelFromString(params("levelID")).getOrElse("Bum")
  }

  put("/levels/:levelID") {
    <p>Saving data for levelID {params("levelID")}</p>
  }

  get("/scores/:levelID") {
    val scoreTotals = ScoresLoader.getScoresFromString(params("levelID"))
    ScoresLoader.jsonStringify(scoreTotals)
  }

  post("/scores/:levelID") {
    ScoresLoader.saveScoresForLevelString(params("levelID"), request.body).getOrElse("failure")
    <p>Let's save stats for level {params("levelID")}</p>
  }

}
