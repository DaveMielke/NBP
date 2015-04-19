import AndroidKeys._

TypedResources.settings

name := "Shell"

organization := "org.nbp"

version := "1.0.0"

scalaVersion := "2.10.0-RC1"

scalacOptions ++= Seq("-deprecation")

AndroidProject.androidSettings

platformName in Android := "android-15"

libraryDependencies ++= Seq(
  "com.j256.ormlite" % "ormlite-android" % "4.42"
)
