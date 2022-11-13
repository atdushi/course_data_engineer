version in ThisBuild := "0.1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.13.10"

//libraryDependencies += "org.scalaj" % "scalaj-http_2.13" % "2.4.2"
libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2"

// https://mvnrepository.com/artifact/net.liftweb/lift-json
libraryDependencies += "net.liftweb" %% "lift-json" % "3.5.0"

lazy val root = (project in file("."))
  .settings(
    name := "FirstProject"
  )
