ThisBuild / scalaVersion := "2.13.14"
ThisBuild / version      := "0.1.0"
ThisBuild / organization := "com.example"

lazy val root = (project in file("."))
  .settings(
    name := "manifest-format-probe",
    libraryDependencies ++= Seq(
      "org.typelevel"  %% "cats-core"  % "2.12.0",
      "org.slf4j"       % "slf4j-api"  % "2.0.13",
      "org.scalatest"  %% "scalatest"  % "3.2.19" % Test
    )
  )
