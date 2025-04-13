ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.18"
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.5.3"
//libraryDependencies ++= Seq(
//  "org.scala-lang" % "scala-library" % scalaVersion.value % "provided",
//  "org.apache.spark" %% "spark-core" % "3.5.3" % "provided"
//)

lazy val root = (project in file("."))
  .settings(
    name := "CoPurchaseAnalysis"
  )
