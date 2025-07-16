import Solutions.{BestSolutionWithPartitions, FirstSolution, GroupByKey, MergeTwoStages, MergeTwoStagesNoFilter, NewPairsMapping}
import Utils.reduceDataset

import java.nio.file.Paths

object Main {

  val defaultInputPath = "src/main/resources/reduced_dataset_5k.csv"
  val defaultOutputPath = "src/main/resources/result"
  val defaultSolution = 5 // Best solution
  val DEBUG = false

  def main(args: Array[String]): Unit = {

    // Example of code used to reduce the dataset
    //    reduceDataset(
    //      "/Path/to/full/dataset.csv",
    //      "/Path/to/save/the/reduced/dataset.csv",
    //      5000 // Number of orders
    //    )

    val inputPath = if (args.length >= 3) args(0)
      else Paths.get(defaultInputPath).toAbsolutePath.toString
    val outputPath = if (args.length >= 3) args(1)
      else Paths.get(defaultOutputPath).toAbsolutePath.toString
    val selectedSolution = if (args.length >= 3) args(2).toInt else defaultSolution
    val numberOfNodes = if (args.length == 4 && selectedSolution == 5) args(3).toInt else 1

    val solutions = List(FirstSolution,GroupByKey,NewPairsMapping,MergeTwoStages,MergeTwoStagesNoFilter)

    selectedSolution match {
      case 0 => Utils.time(FirstSolution.run(inputPath, outputPath, DEBUG))
      case 1 => Utils.time(GroupByKey.run(inputPath, outputPath, DEBUG))
      case 2 => Utils.time(NewPairsMapping.run(inputPath, outputPath, DEBUG))
      case 3 => Utils.time(MergeTwoStages.run(inputPath, outputPath, DEBUG))
      case 4 => Utils.time(MergeTwoStagesNoFilter.run(inputPath, outputPath, DEBUG))
      case 5 => Utils.time(BestSolutionWithPartitions.run(inputPath, outputPath, DEBUG, numberOfNodes))
      case 99 => Utils.testAllSolutions(solutions, inputPath, outputPath, DEBUG = false)
    }

    if (DEBUG) System.in.read // Keep the Spark UI active
  }
}