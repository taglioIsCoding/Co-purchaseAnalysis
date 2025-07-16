import solutions.Solution
import org.apache.spark.{SparkConf, SparkContext}

object Utils {

  // Creates a reduced version of the dataset
  def reduceDataset(inputFile: String, outputFile: String, last_id: Int): Unit = {
    val conf = new SparkConf()
      .setAppName("DataSetReducer")
      .set("spark.hadoop.validateOutputSpecs", "false") // Replace output folder if exists
      .setMaster("local[4]")

    val allLinesBeforeId = new SparkContext(conf)
      .textFile(inputFile)
      .filter(line => line.split(",")(0).toInt <= last_id)

    allLinesBeforeId.saveAsTextFile(outputFile)
  }

  // Measure the wall time clock of a block
  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0)/1000000 + "ms")
    result
  }

  // Measure 10 times the wall time clock
  def testSolution[R](block: => R): Unit = {
    var total: Long = 0
    for (i <- 0 to 10) {
      val t0 = System.nanoTime()
      block // call-by-name
      val t1 = System.nanoTime()
      total = total + (t1 - t0)
    }

    total = total / 10
    println("Average Elapsed time: " + total / 1000000 + "ms")
  }

  // Test each solution
  def testAllSolutions(solutions: List[Solution], inputPath: String, outputPath: String, DEBUG: Boolean) = {
    solutions.map( solution => {
        println(solution.name)
        testSolution(solution.run(inputPath, outputPath, DEBUG))
      }
    )
  }
}
