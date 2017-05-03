val sparkVersion = "2.1.0"
  
val projectName = "Spark-examples"

val dependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
) 
   
lazy val main = Project(projectName, base = file("."))
  .settings(libraryDependencies ++= dependencies) 
