name := "PracticalFpInScala"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Dependency()

scalacOptions += "-Ymacro-annotations"

scalacOptions --= Seq(
  "-Xlint:nullary-override",
)