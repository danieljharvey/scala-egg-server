package com.danieljharvey.eggserver

import java.sql.{Connection,DriverManager}
import scala.util.Try
import scala.util.Properties

import scala.collection.mutable.ListBuffer

object MySQL {

  def dbOptions = Map(
    "url" -> scala.util.Properties.envOrElse("DB_URL", "jdbc:mysql://localhost/mysql"),
    "driver" -> "com.mysql.jdbc.Driver",
    "username" -> scala.util.Properties.envOrElse("USERNAME", "username"),
    "password" -> scala.util.Properties.envOrElse("PASSWORD", "password")
  )

  def getLevel (levelID: Int) : Option[String] = {
    lazy val ctx = new MysqlJdbcContext(SnakeCase, "ctx")
    try {
      Class.forName(dbOptions("driver"))
      connection = DriverManager.getConnection(dbOptions("url"), dbOptions("username"), dbOptions("password"))
      def sql = "SELECT * FROM levels WHERE levelID = ?"
      val preparedStatement = connection.prepareStatement(sql)
      preparedStatement.setInt(1, levelID)

      // execute select SQL stetement
      val rs = preparedStatement.executeQuery()
      if(rs.next) {
        val result = Some(rs.getString("data"))
        connection.close
        result
      }
    } catch {
      case e: Exception => e.printStackTrace
    }
    return None
  }

  type ScoreRow = Map[String,Int]

  type ScoreList = List[ScoreRow]

  def getScoresForLevel (levelID: Int) : Option[ScoreList] = {
    var connection:Connection = null
    try {
      Class.forName(dbOptions("driver"))
      connection = DriverManager.getConnection(dbOptions("url"), dbOptions("username"), dbOptions("password"))
      def sql = "SELECT * FROM scores WHERE levelID = ?"
      var preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, levelID);

      // execute select SQL stetement
      var rs = preparedStatement.executeQuery();
      connection.close

      var buffer = ListBuffer[ScoreRow]()
      while (rs.next) {
        val row = Map(
          "rotationsUsed" -> rs.getInt("rotationsUsed"),
          "score" -> rs.getInt("score")
        )
        buffer += row
      }
      println(buffer)
      //val fixedRows:List[Map[String,Int]] = rows
      return Some(buffer.toList)
    } catch {
      case e: Exception => e.printStackTrace
    }
    return None
  }

}