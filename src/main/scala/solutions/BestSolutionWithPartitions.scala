package solutions

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object BestSolutionWithPartitions extends Solution {

  override def run(inputPath: String, outputPath: String, DEBUG: Boolean): Unit = {
    run(inputPath, outputPath, DEBUG, 4)
  }

  def run(inputPath: String, outputPath: String, DEBUG: Boolean, numberOfNodes: Int = 1): Unit = {
    val conf = new SparkConf()
      .set("spark.hadoop.validateOutputSpecs", "false") // Replace output folder if exists
      .setAppName("CoPurchase")

    val context = new SparkContext(conf)

    val corePerNode = 4
    val partitionPerCore = 2
    val numberOfPartitions = numberOfNodes * corePerNode * partitionPerCore

    val orders = context
      .textFile(inputPath)
      .map(csvToOrder)
      .partitionBy(new HashPartitioner(numberOfPartitions))
      .groupByKey(numberOfPartitions)

    if (DEBUG) orders.foreach(println(_))

    val orderCombinations: RDD[((Int,Int), Int)] =
    orders.flatMap(
      order => order._2.flatMap(
        x => order._2.collect({case y if y < x => ((y,x),1)}))
    )

    if (DEBUG) orderCombinations.foreach(println(_))

    val result = orderCombinations
      .reduceByKey((x,y) => x+y)
      .map({case ((x,y),n) => s"$x,$y,$n"})
    result.repartition(1).saveAsTextFile(outputPath)
    if (!DEBUG) context.stop()
  }

  private def csvToOrder(line: String) = {
    val split = line.split(',')
    (split(0).toInt, split(1).toInt)
  }
}
