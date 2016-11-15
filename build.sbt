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
  .aggregate(firstSteps, akkaStreams, akkaCamel, akkaHttp, akkaPersistence, akkaCluster)

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

lazy val `akka-http` = (project in file("akka-http"))
  .settings(commonSettings)
  .settings(
    name := """akka-http""",
    version := "1.0",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http-core" % "2.4.11",
      "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11",
      "com.typesafe.akka" %% "akka-http-jackson-experimental" % "2.4.11",
      "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.11",
      "com.typesafe.akka" %% "akka-http-xml-experimental" % "2.4.11"
    )
  )
  .dependsOn(`learning-akka` % "test->test;compile->compile")

lazy val akkaHttp = LocalProject("akka-http")

lazy val `akka-persistence` = (project in file("akka-persistence"))
  .settings(commonSettings)
  .settings(
    name := """akka-persistence""",
    version := "1.0",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence-query-experimental" % akkaVersion,
      "org.iq80.leveldb" % "leveldb" % "0.7",
      "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
    )
  )
  .dependsOn(`learning-akka` % "test->test;compile->compile")

lazy val akkaPersistence = LocalProject("akka-persistence")

lazy val `akka-cluster` = (project in file("akka-cluster"))
  .settings(commonSettings)
  .settings(
    name := """akka-cluster""",
    version := "1.0",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-remote" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion
    )
  )
  .dependsOn(`learning-akka` % "test->test;compile->compile")

lazy val akkaCluster = LocalProject("akka-cluster")





