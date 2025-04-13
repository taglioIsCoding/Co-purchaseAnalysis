import org.apache.spark.{SparkConf, SparkContext}

case class Order(order_id: Int, product_id: Int)

object Solutions {
    def first_solution(inputPath: String, outputPath: String): Unit = {
      val conf = new SparkConf()
        .setAppName("CoPurchase")
        .setMaster("local[4]")
      val context = new SparkContext(conf)

      val orders = ordersWithProductsList(context, inputPath)

      val orderCombinations = orders.map[(Int, Iterable[(Int,Int)])](order =>
        (order._1, order._2.flatMap(x => order._2.map(y => (y,x)).filter(product_ids => product_ids._1<product_ids._2))))


      val allPurchasesWithOne = orderCombinations.flatMap(order => order._2.map(x => (x,1)))

      val result = allPurchasesWithOne.reduceByKey((x, y) => x + y)
      result.saveAsTextFile(outputPath)
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
