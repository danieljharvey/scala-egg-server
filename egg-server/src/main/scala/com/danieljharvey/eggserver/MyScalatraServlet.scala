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
    Levels.getLevelFromString(params("levelID"))
  }

  put("/levels/:levelID") {
    <p>Saving data for levelID {params("levelID")}</p>
  }

  get("/scores/:levelID") {
   Scores.getScoresFromString(params("levelID"))
  }

  put("/scores/:levelID") {
    <p>Let's save stats for level {params("levelID")}</p>
  }

}
