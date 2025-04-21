package Solutions

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MergeTwoStagesNoFilter extends Solution {

  def run(inputPath: String, outputPath: String, DEBUG: Boolean): Unit = {
    val conf = new SparkConf()
      .set("spark.hadoop.validateOutputSpecs", "false") // Replace output folder if exists
      .setAppName("CoPurchase")

    val context = new SparkContext(conf)

    val orders = context
      .textFile(inputPath)
      .map(csvToOrder)
      .groupByKey()

    if (DEBUG) orders.foreach(println(_))

    val orderCombinations: RDD[((Int,Int), Int)] =
    orders.flatMap(
      order => order._2.flatMap(
        x => order._2.collect({case y if y < x => ((y,x),1)}))
    )

    if (DEBUG) orderCombinations.foreach(println(_))

    val result = orderCombinations.reduceByKey((x,y) => x+y)
    result.saveAsTextFile(outputPath)
    if (!DEBUG) context.stop()
  }

  private def csvToOrder(line: String) = {
    val split = line.split(',')
    (split(0).toInt, split(1).toInt)
  }
}
