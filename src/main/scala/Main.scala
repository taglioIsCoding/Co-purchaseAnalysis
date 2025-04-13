import org.apache.spark.{SparkConf, SparkContext}

object Main {

  val defaultInputPath = "/Users/micheletagliani/Developer/Scala/playground/CoPurchaseAnalysis/src/main/resources/reduced_dataset.csv"
  val defaultOutputPath = "/Users/micheletagliani/Developer/Scala/playground/CoPurchaseAnalysis/src/main/resources/result"

  def main(args: Array[String]): Unit = {
    val inputPath = if (args.length == 2) args(0) else defaultInputPath
    val outputPath = if (args.length == 2) args(1) else defaultOutputPath

    Utils.time(Solutions.first_solution(inputPath, outputPath))

    System.in.read // Keep the Spark UI UP
  }
}