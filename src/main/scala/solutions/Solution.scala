package solutions

case class Order(order_id: Int, product_id: Int)

trait Solution {
  val name: String = getClass.getName.split('.')(1)
  def run(inputPath: String, outputPath: String, DEBUG: Boolean): Unit
}
