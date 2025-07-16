package solutions

import org.apache.spark.{SparkConf, SparkContext}

object FirstSolution extends Solution {
  def run(inputPath: String, outputPath: String, DEBUG: Boolean): Unit = {
    val conf = new SparkConf()
      .set("spark.hadoop.validateOutputSpecs", "false") // Replace output folder if exists
      .setAppName("CoPurchase")

    val context = new SparkContext(conf)

    val orders = ordersWithProductsList(context, inputPath)

    if (DEBUG) orders.foreach(println(_))

    val orderCombinations = orders.map[(Int, Iterable[(Int,Int)])](order =>
      (order._1, order._2.flatMap(x => order._2.map(y => (y,x)).filter(product_ids => product_ids._1<product_ids._2))))

    if (DEBUG) orderCombinations.foreach(println(_))

    val allPurchasesWithOne = orderCombinations.flatMap(order => order._2.map(x => (x,1)))

    if (DEBUG) allPurchasesWithOne.foreach(println(_))

    val result = allPurchasesWithOne
      .reduceByKey((x,y) => x+y)
      .map({case ((x,y),n) => s"$x,$y,$n"})
    result.repartition(1).saveAsTextFile(outputPath)

    if (!DEBUG) context.stop()
  }

  private def csvToOrder(line: String) = {
    val split = line.split(',')
    Order(split(0).toInt, split(1).toInt)
  }

  private def ordersWithProductsList(context: SparkContext, inputPath:String) = {
    context.textFile(inputPath)
      .map(csvToOrder)
      .groupBy(_.order_id)
      .mapValues(
        _.map(_.product_id)
      )
  }
}
