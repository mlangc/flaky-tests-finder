name := "flaky-tests-finder"

version := "1.0.0-SNAPSHOT"

organization := "com.github.mlangc"

scalaVersion := "2.11.8"

scalacOptions := Seq("-encoding", "utf8", "-feature", "-deprecation", "-optimise", "-target:jvm-1.8", "-Ywarn-unused", "-Ywarn-dead-code", "-Ywarn-unused-import")

EclipseKeys.withSource := true

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test" 

libraryDependencies += "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.5"

libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.5"

libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.5"