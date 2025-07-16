import solutions.{BestSolutionWithPartitions, FirstSolution, GroupByKey, MergeTwoStages, MergeTwoStagesNoFilter, NewPairsMapping}
import Utils.reduceDataset

object Main {

  val DEBUG = false
  val defaultNumberOfNodes = 1

  def main(args: Array[String]): Unit = {

    // Example of code used to reduce the dataset
    //    reduceDataset(
    //      "/Path/to/full/dataset.csv",
    //      "/Path/to/save/the/reduced/dataset.csv",
    //      5000 // Number of orders
    //    )

    if (args.length < 3) {
      println("Usage: <inputPath> <outputPath> <solutionId> <numberOfNodes>")
      sys.exit(1)
    }

    val inputPath = args(0)
    val outputPath = args(1)

    val selectedSolution = try {
      args(2).toInt
    } catch {
      case _: NumberFormatException =>
        println(s"Error: '${args(2)}' is not a valid integer for solutionId.")
        sys.exit(1)
    }

    val numberOfNodes = if (args.length > 3) {
      try {
        args(3).toInt
      } catch {
        case _: NumberFormatException =>
          println(s"Error: '${args(3)}' is not a valid integer for numberOfNodes.")
          sys.exit(1)
      }
    } else {
      defaultNumberOfNodes
    }


    selectedSolution match {
      case 0 => Utils.time(FirstSolution.run(inputPath, outputPath, DEBUG))
      case 1 => Utils.time(GroupByKey.run(inputPath, outputPath, DEBUG))
      case 2 => Utils.time(NewPairsMapping.run(inputPath, outputPath, DEBUG))
      case 3 => Utils.time(MergeTwoStages.run(inputPath, outputPath, DEBUG))
      case 4 => Utils.time(MergeTwoStagesNoFilter.run(inputPath, outputPath, DEBUG))
      case 5 => Utils.time(BestSolutionWithPartitions.run(inputPath, outputPath, DEBUG, numberOfNodes))
      case _ =>
        println(s"Error: '${args(2)}' is not a valid solutionId. The valid ones are between 0 and 5")
        sys.exit(1)
    }

    if (DEBUG) System.in.read // Keep the Spark UI active
  }
}