ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / organization     := "com.github.arturopala"
ThisBuild / organizationName := "Artur Opala"
ThisBuild / startYear := Some(2020)

lazy val root = (project in file("."))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(
    name := "balloon-burst",
    licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.1.1" % Test
    ),
    excludeFilter in (Compile, unmanagedResources) := NothingFilter,
    scalafmtOnCompile in Compile := true,
    scalafmtOnCompile in Test := true,
    mainClass in (Compile, packageBin) := Some("com.github.arturopala.balloonburst.BalloonBurstCommandLine")
  )