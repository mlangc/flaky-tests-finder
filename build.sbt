name := "flaky-tests-finder"

version := "1.0.0-SNAPSHOT"

organization := "com.github.mlangc"

scalaVersion := "2.11.8"

scalacOptions := Seq("-encoding", "utf8", "-feature", "-deprecation", "-optimise", "-target:jvm-1.8", "-Ywarn-unused", "-Ywarn-dead-code", "-Ywarn-unused-import")

EclipseKeys.withSource := true

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

libraryDependencies += "com.beachape" %% "enumeratum" % "1.4.1"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test" 

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.0" % "test"

libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.2.2" % "provided"
