name := "SustainConnector"

version := "1.0"

scalaVersion := "2.12.12"

libraryDependencies ++= Seq(
  "org.mongodb.spark" %% "mongo-spark-connector" % "3.0.0",
  "org.apache.spark" %% "spark-core" % "3.0.0",
  "org.apache.spark" %% "spark-sql" % "3.0.0",
  "org.apache.spark" %% "spark-mllib" % "3.0.0"
)
