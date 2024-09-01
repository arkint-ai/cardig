// Add this line at the top of your build.sbt file
enablePlugins(AssemblyPlugin)
import sbtassembly.MergeStrategy

val scala3Version = "3.5.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Scala 3 Project Template",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies ++= Seq(
     "org.jsoup" % "jsoup" % "1.15.3",
     "net.ruippeixotog" %% "scala-scraper" % "3.0.0",
     "org.seleniumhq.selenium" % "selenium-java" % "4.5.0"
   )
  )

// Configuration for sbt-assembly
assemblyMergeStrategy in assembly := {
  case PathList("module-info.class") => MergeStrategy.discard
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
  case "META-INF/io.netty.versions.properties" => MergeStrategy.first
  case "META-INF/LICENSE" => MergeStrategy.first
  case "META-INF/NOTICE" => MergeStrategy.first
  case x => MergeStrategy.defaultMergeStrategy(x)
}
