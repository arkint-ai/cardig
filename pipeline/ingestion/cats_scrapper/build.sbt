val scala3Version = "3.5.0"

lazy val root = project
  .in(file("."))
  .settings(
    name                                   := "cats_scrapper",
    version                                := "0.0.1",
    scalaVersion                           := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies ++= Seq(
      "org.jsoup"                   % "jsoup"           % "1.18.1",
      "org.typelevel"              %% "cats-core"       % "2.9.0",
      "org.typelevel"              %% "cats-effect"     % "3.4.0",
      "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.5",
      "org.slf4j"                   % "slf4j-api"       % "2.0.16",
      "ch.qos.logback"              % "logback-classic" % "1.5.6"
    ),
    scalacOptions ++= Seq("-explain")
  )
