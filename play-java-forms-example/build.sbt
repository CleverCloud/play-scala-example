name := """play-java-forms-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

crossScalaVersions := Seq("2.13.10", "3.3.0-RC3")

scalaVersion := crossScalaVersions.value.head

(Test / testOptions) := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

libraryDependencies += guice

scalacOptions ++= List("-Werror")
javacOptions ++= Seq(
  "-Xlint:unchecked",
  "-Xlint:deprecation",
  "-Werror"
) 
