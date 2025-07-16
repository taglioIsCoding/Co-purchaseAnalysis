package Solutions

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MergeTwoStagesWithFilter extends Solution {

  def run(inputPath: String, outputPath: String, DEBUG: Boolean): Unit = {
    val conf = new SparkConf()
      .set("spark.hadoop.validateOutputSpecs", "false") // Replace output folder if exists
      .setAppName("CoPurchase")

    val context = new SparkContext(conf)
    context.setLogLevel("ERROR")
    val orders = context
      .textFile(inputPath)
      .map(csvToOrder)
      .groupByKey()

    if (DEBUG) orders.foreach(println(_))

    val orderCombinations = orders.flatMap(order =>
      for {
        a <- order._2
        b <- order._2
        if a < b
      } yield ((a,b), 1)
    )

    if (DEBUG) orderCombinations.foreach(println(_))

    val result = orderCombinations.reduceByKey((x,y) => x+y)
      .map({case ((x,y),n) => s"$x,$y,$n"})
    result.coalesce(1).saveAsTextFile(outputPath)
    if (!DEBUG) context.stop()
  }

  private def csvToOrder(line: String) = {
    val split = line.split(',')
    (split(0).toInt, split(1).toInt)
  }
}
