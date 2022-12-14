ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.17"

//libraryDependencies += "org.apache.spark" %% "spark-core" % "3.3.1"
//libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.3.1"
//libraryDependencies += "org.postgresql" % "postgresql" % "42.5.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.3.1",
  "org.apache.spark" %% "spark-sql" % "3.3.1",
  "org.postgresql" % "postgresql" % "42.5.0"
)

lazy val root = (project in file("."))
  .settings(
    name := "HelloSpark"
  )
