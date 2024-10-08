val scala3Version = "3.5.0"

lazy val root = project
  .in(file("."))
  .settings(
    name         := "cats_scrapper",
    version      := "0.0.1",
    scalaVersion := scala3Version,
    // libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies += "org.typelevel" %% "munit-cats-effect" % "2.0.0" % "test",
    libraryDependencies ++= Seq(
      //"org.jsoup"                   % "jsoup"               % "1.18.1",
      "org.typelevel"               %% "cats-core"           % "2.9.0",
      "org.typelevel"               %% "cats-effect"         % "3.4.0",
      "com.typesafe.scala-logging"  %% "scala-logging"       % "3.9.5",
      "org.slf4j"                   % "slf4j-api"           % "2.0.16",
      "ch.qos.logback"              % "logback-classic"     % "1.5.6",
      "org.http4s"                  %% "http4s-dsl"          % "0.23.28",
      "org.http4s"                 %% "http4s-ember-client" % "0.23.28",
      //"org.http4s"                 %% "http4s-ember-server" % "0.23.28",
      "org.http4s"                 %% "http4s-dsl"          % "0.23.28",
      "org.http4s"                 %% "http4s-circe"        % "0.23.28",
      "io.circe"                   %% "circe-generic"       % "0.14.1",
      "io.circe"                   %% "circe-parser"        % "0.14.1"
    ),
    scalacOptions ++= Seq("-explain")
  )
