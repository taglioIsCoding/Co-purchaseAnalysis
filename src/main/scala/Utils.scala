import org.apache.spark.{SparkConf, SparkContext}

object Utils {

  def reduceDataset(inputFile: String, outputFile: String, last_id: Int): Unit = {
    val conf = new SparkConf().setAppName("DataSetReducer").
      setMaster("local[4]")

    val allLinesBeforeId = new SparkContext(conf)
      .textFile(inputFile)
      .filter(line => line.split(",")(0).toInt <= last_id)

    allLinesBeforeId.saveAsTextFile(outputFile)
  }

  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0)/1000000 + "ms")
    result
  }
}
