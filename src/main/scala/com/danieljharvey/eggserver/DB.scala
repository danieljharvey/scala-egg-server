package com.danieljharvey.eggserver

import io.getquill._

package object db {

  lazy val ctx = new MysqlJdbcContext(CamelCase, "ctx")

  import ctx._
  
}