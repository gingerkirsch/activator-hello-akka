import sbt._

lazy val akkaVersion = "2.4.12"

val commonSettings = Seq(
  scalaVersion := "2.11.8",
  testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    "org.scalatest" %% "scalatest" % "2.2.6" % "test",
    "junit" % "junit" % "4.12" % "test",
    "com.novocode" % "junit-interface" % "0.11" % "test"
  )
)

lazy val `learning-akka` = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := """learning-akka"""
  )
  .aggregate(firstSteps, akkaStreams, akkaCamel)

lazy val `first-steps` = (project in file("first-steps"))
  .settings(commonSettings)
  .settings(
    name := """first-steps""",
    version := "1.0"
  )
  .dependsOn(`learning-akka` % "test->test;compile->compile")

lazy val firstSteps = LocalProject("first-steps")

lazy val `akka-streams` = (project in file("akka-streams"))
  .settings(commonSettings)
  .settings(
    name := """akka-streams""",
    version := "1.0",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
      "org.twitter4j" % "twitter4j-stream" % "4.0.3"
    )
  )
  .dependsOn(`learning-akka` % "test->test;compile->compile")

lazy val akkaStreams = LocalProject("akka-streams")

lazy val `akka-camel` = (project in file("akka-camel"))
  .settings(commonSettings)
  .settings(
    name := """akka-camel""",
    version := "1.0",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-camel" % akkaVersion,
      "org.apache.camel" % "camel-jetty" % "2.10.3",
      "org.apache.camel" % "camel-quartz" % "2.10.3",
      "org.slf4j" % "slf4j-api" % "1.7.2",
      "ch.qos.logback" % "logback-classic" % "1.0.7"
    )
  )
  .dependsOn(`learning-akka` % "test->test;compile->compile")

lazy val akkaCamel = LocalProject("akka-camel")





