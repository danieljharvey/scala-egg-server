val ScalatraVersion = "2.6.2"

organization := "com.github.danieljharvey"

name := "Egg Server"

version := "0.0.1"

scalaVersion := "2.12.4"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "mysql" % "mysql-connector-java" % "5.1.38",
  "io.getquill" %% "quill-jdbc" % "2.3.1",
  "net.liftweb" %% "lift-json" % "3.0.2",
  "com.kailuowang" %% "henkan-convert" % "0.4.4",
  "com.kailuowang" %% "henkan-optional" % "0.4.4"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
