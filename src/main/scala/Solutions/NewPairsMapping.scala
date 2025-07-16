package Solutions

import org.apache.spark.{SparkConf, SparkContext}

object NewPairsMapping extends Solution {

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

    val orderCombinations = orders.map[(Int, Iterable[Any])](order =>
      (order._1,
        order._2.flatMap(x => order._2.map(y => if(y < x) (y,x))).filterNot(_.equals(()))))

    if (DEBUG) orderCombinations.foreach(println(_))

    val allPurchasesWithOne = orderCombinations.flatMap(order => order._2.map(x => (x,1)))

    if (DEBUG) allPurchasesWithOne.foreach(println(_))

    val result = allPurchasesWithOne.reduceByKey((x, y) => x + y)
      .map({case ((x,y),n) => s"$x,$y,$n"})
    result.repartition(1).saveAsTextFile(outputPath)
    if (!DEBUG) context.stop()
  }

  private def csvToOrder(line: String) = {
    val split = line.split(',')
    (split(0).toInt, split(1).toInt)
  }
}
