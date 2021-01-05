# sustain-spark-connector
Provides an interface to run Spark jobs on, using MongoDB as a source of data.

## Description

This project is written in Scala, and is Scala Build Tool (SBT) compliant. Currently, it only contains a single source code file, `src/main/scala/SustainConnector.scala`, which imports a small dataset from the Mongo database and trains/tests a simple linear regression model on the hospital area population to hospital beds data. Ultimately, this project will provide an interface for users to request Apache Spark jobs to be run on Documents/Collections pulled in from the SUSTAIN database (as DataFrames), allowing valuable insights to be calculated using the performance advantages that Spark's in-memory framework provides.


## Usage

* Compile the project: `sbt compile`
* Run the project: `sbt run`

__Note__ By default, the project uses the standalone Spark master (local) with 4 cores allocated. If you wish to modify this, perhaps to use a managed cluster, the `SparkSessionBuilder`'s `.master()` must be changed to point to your cluster manager.
