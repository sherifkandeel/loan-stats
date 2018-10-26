val circeVersion        = "0.9.3"
val http4sVersion       = "0.18.19"
val specs2Version       = "4.3.4"
val scalaMockVersion    = "4.1.0"
val scalaLoggingVersion = "3.9.0"
val configVersion       = "1.3.3"
val mockServerVersion   = "5.3.0"
val LogbackVersion      = "1.2.3"

lazy val root = (project in file(".")).settings(
  name := "spotcap-task",
  version := "0.1",
  scalaVersion := "2.12.7",
  libraryDependencies ++= Seq(
    "io.circe"                   %% "circe-parser"        % circeVersion,
    "io.circe"                   %% "circe-literal"       % circeVersion,
    "org.http4s"                 %% "http4s-blaze-server" % http4sVersion,
    "org.http4s"                 %% "http4s-circe"        % http4sVersion,
    "org.http4s"                 %% "http4s-dsl"          % http4sVersion,
    "org.specs2"                 %% "specs2-core"         % specs2Version % "test",
    "org.specs2"                 %% "specs2-scalacheck"   % specs2Version % "test",
    "org.scalamock"              %% "scalamock"           % scalaMockVersion % Test,
    "org.mock-server"            % "mockserver-netty"     % mockServerVersion % "test",
    "com.typesafe.scala-logging" %% "scala-logging"       % scalaLoggingVersion,
    "com.typesafe"               % "config"               % configVersion,
    "ch.qos.logback"             % "logback-classic"      % LogbackVersion
  ),
  addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.6"),
  addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")
)
