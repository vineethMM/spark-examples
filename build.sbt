val sparkVersion = "2.1.0"

val projectName = "Spark-examples"

val dependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
  // test
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
) 
   
lazy val main = Project(projectName, base = file("."))
  .settings(libraryDependencies ++= dependencies)
  .settings(scalaVersion := "2.11.8")

coverageEnabled := true

parallelExecution in ThisBuild := false