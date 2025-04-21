import Solutions.{FirstSolution, GroupByKey, MergeTwoStages, MergeTwoStagesNoFilter, NewPairsMapping}
import Utils.reduceDataset
import org.apache.spark.{SparkConf, SparkContext}

object Main {

  val defaultInputPath = "/Users/micheletagliani/Developer/Scala/playground/CoPurchaseAnalysis/src/main/resources/reduced_dataset_5k.csv"
  val defaultOutputPath = "/Users/micheletagliani/Developer/Scala/playground/CoPurchaseAnalysis/src/main/resources/result"
  val DEBUG = false

  def main(args: Array[String]): Unit = {

//    reduceDataset(
//      "/Users/micheletagliani/Developer/Scala/playground/CoPurchaseAnalysis/src/main/resources/order_products.csv",
//      "/Users/micheletagliani/Developer/Scala/playground/CoPurchaseAnalysis/src/main/resources/reduced_dataset",
//      5000
//    )

    val inputPath = if (args.length == 3) args(0) else defaultInputPath
    val outputPath = if (args.length == 3) args(1) else defaultOutputPath
    val selectedSolution = if (args.length == 3) args(2).toInt else 4

    val solutions = List(FirstSolution,GroupByKey, NewPairsMapping,MergeTwoStages,MergeTwoStagesNoFilter)

    selectedSolution match {
      case 0 => Utils.time(FirstSolution.run(inputPath, outputPath, DEBUG))
      case 1 => Utils.time(GroupByKey.run(inputPath, outputPath, DEBUG))
      case 2 => Utils.time(NewPairsMapping.run(inputPath, outputPath, DEBUG))
      case 3 => Utils.time(MergeTwoStages.run(inputPath, outputPath, DEBUG))
      case 4 => Utils.time(MergeTwoStagesNoFilter.run(inputPath, outputPath, DEBUG))
      case 99 => Utils.testAllSolutions(solutions, inputPath, outputPath, DEBUG = false)
    }

    if (DEBUG) System.in.read // Keep the Spark UI active
  }
}