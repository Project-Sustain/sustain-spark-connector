/* -----------------------------------------------
 * SustainConnector.scala -
 *
 * Description:
 *    Provides a demonstration of the Spark MongoDB Connector, by
 *    implementing a linear regression model on one of the SUSTAIN
 *    datasets in the MongoDB sharded cluster.
 *    Guide for this project taken directly from MongoDB docs:
 *    https://docs.mongodb.com/spark-connector/master/scala-api
 *
 *  Author:
 *    Caleb Carlson
 *
 * ----------------------------------------------- */


object SustainConnector {

  /* Entrypoint for the application */
  def main(args: Array[String]): Unit = {

    /* Minimum Imports */
    import com.mongodb.spark.config.ReadConfig
    import com.mongodb.spark.sql.DefaultSource
    import com.mongodb.spark.MongoSpark
    import org.apache.spark.sql.SparkSession
    import org.apache.spark.sql.DataFrame
    import org.apache.spark.sql.Dataset
    import org.apache.spark.sql.Row
    import org.apache.spark.ml.feature.{VectorAssembler, StringIndexer}
    import org.apache.spark.ml.regression.LinearRegression
    import org.apache.spark.ml.regression.LinearRegressionModel
    import org.apache.spark.ml.evaluation.RegressionEvaluator
    import org.apache.spark.sql.functions.col

    // Create the SparkSession and ReadConfig
    val sparkConnector: SparkSession = SparkSession.builder()
      .master("local")
      .appName("SustainConnector")
      .config("spark.mongodb.input.uri", "mongodb://127.0.0.1:27017/")
      .config("spark.mongodb.input.database", "sustain")
      .config("spark.mongodb.input.collection", "hospitals_geo")
      .getOrCreate()

    import sparkConnector.implicits._ // For the $()-referenced columns

    //val readConfig: ReadConfig = ReadConfig(Map("uri" -> "mongodb://127.0.0.1/",
    //                                            "database" -> "sustain", 
    //                                            "collection" -> "hospitals_geo"))

    // Read collection into a DataFrame
    var df: DataFrame = MongoSpark.load(sparkConnector)
    
    /* Tutorial used from the link below for linear regression modeling:
     * https://medium.com/@awabmohammedomer/how-to-fit-a-linear-regression-model-in-apache-spark-using-scala-f246dd06a3b1
     */

    // Select columns and rename them to appropriate labels
    df = df.select($"_id", $"properties"("POPULATION"), $"properties"("BEDS"))
      .withColumnRenamed("properties.BEDS", "label")
      .withColumnRenamed("properties.POPULATION", "POPULATION")

    // Create a feature transformer that merges multiple columns into a vector column
    var assembler: VectorAssembler = new VectorAssembler()
      .setInputCols(Array("POPULATION"))
      .setOutputCol("features")

    // Merge multiple feature columns into a single vector column
    df = assembler.transform(df)

    // Split input into testing set and training set:
    // 80% training, 20% testing, with random seed of 42
    var Array(train, test): Array[Dataset[Row]] = df.randomSplit(Array(0.8, 0.2), 42)

    // Create a linear regression model object and fit it to the training set
    var linearRegression: LinearRegression = new LinearRegression()
    var lrModel: LinearRegressionModel = linearRegression.fit(train)
  
    // Use the model on the testing set, and evaluate results
    var lrPredictions: DataFrame = lrModel.transform(test)
    val evaluator: RegressionEvaluator = new RegressionEvaluator().setMetricName("rmse")
    evaluator.evaluate(lrPredictions)
  
  }

}
