#!/bin/bash

# Run SBT clean/compile/packaging utilities
sbt clean assembly

SCALA_VERSION="2.12"

# Submission Variables
MAIN_CLASS="SustainConnector"
SPARK_MASTER="spark://lattice-167:8079"
DEPLOY_MODE="cluster"


APPLICATION_JAR="./target/scala-${SCALA_VERSION}/SustainConnector-assembly-1.0.jar"

if [[ -f $APPLICATION_JAR ]]; then
  echo -e "\n>>> SBT JAR successfully created, submitting to Spark\n"
  spark-submit \
    --class       $MAIN_CLASS   \
    --master      $SPARK_MASTER \
    --deploy-mode $DEPLOY_MODE  \
    $APPLICATION_JAR
fi
