version := "1.0-SNAPSHOT"
organization in ThisBuild := "com.lihaoyi"
scalaVersion in ThisBuild := "2.11.8"

lazy val server = (project in file("server"))
  .enablePlugins(PlayScala)
  .settings(
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.4.3"
  )
  .dependsOn(sharedJvm)

lazy val client = (project in file("client"))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .settings(
    persistLauncher := true,
    persistLauncher in Test := false,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "com.lihaoyi" %%% "upickle" % "0.4.3"
    )
  )
  .dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
