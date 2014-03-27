
name := "future-talk"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.10.3",
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.2.3",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "org.apache.lucene" % "lucene-core" % "4.6.1",
  "org.apache.lucene" % "lucene-analyzers-common" % "4.6.1",
  "io.spray" % "spray-can" % "1.2.0",
  "io.spray" % "spray-routing" % "1.2.0",
  "io.spray" %% "spray-json" % "1.2.5",
  "io.spray" % "spray-testkit" % "1.2.0" % "test",
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.1" % "test",
  "org.specs2" %% "specs2" % "1.14" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.3" % "test",
  "com.novocode" % "junit-interface" % "0.7" % "test->default"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.6",
  "-encoding", "UTF-8"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
