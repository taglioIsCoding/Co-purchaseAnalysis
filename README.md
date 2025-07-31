# Co-purchase Analysis
Repository of the code produced for the Scalable and Cloud Programming course. 

## Project Structure 
- [solutions](./src/main/scala/solutions) folder: all the explored solutions 
- [resources](./src/main/resources) folder: reduced version of the dataset used to locally compare the solutions 
- [data_analysis](./data_analysis/data-analysis.ipynb) folder: Python scripts used to do data analysis

## Solutions 
The program can be used with four parameters:
- the path of the input file 
- the path of the output folder 
- the solution code 
- the number of nodes

The only solution that supports the _number_of_nodes_ param is BestSolutionWithPartitions. 
All other solutions will ignore the parameter and so it can be omitted. 
Each explored solution has a _solution_id_ that can be used as a parameter when running the project.
The available solutions are: <br>

| Solution Id | Solution Name                     |
|-------------|-----------------------------------|
| 0           | FirstSolution                     |
| 1           | GroupByKey                        |
| 2           | NewPairsMapping                   |
| 3           | MergeTwoStages                    |
| 4           | MergeTwoStagesNoFilter            |
| 5           | BestSolutionWithPartitions        |

## Run on DataProc
1. Create a new project on Google Cloud Platform
2. Enable Dapaproc and the APIs
3. Create and download the service account keys 
4. Export useful variables:
    ```
    export GOOGLE_APPLICATION_CREDENTIALS="/path/to/key.json" \
    export PROJECT=<project-id> \
    export BUCKET_NAME=<bucket-name> \
    export CLUSTER=<cluster-name> \
    export REGION=<region>
   ```
5. Clone this repository:
   ```
   git clone https://github.com/taglioIsCoding/Co-purchaseAnalysis/
    ```
6. Initialize the Google Cloud project:
   ```
   gcloud init
   ```
7. Create the bucket:
    ```
   gcloud storage buckets create gs://${BUCKET_NAME} --location=${REGION}
   ```
8. Create the cluster: <br>
   1. With a single node
    ```
   gcloud dataproc clusters create ${CLUSTER} \
    --project=${PROJECT} \
    --region=${REGION} \
    --single-node \
    --master-boot-disk-size 240
   ```
   2. With multiple nodes 
   ```
   gcloud dataproc clusters create ${CLUSTER} \
   --region=${REGION} \
   --num-workers=<number-of-workers> \
   --master-boot-disk-size 240 \
   --worker-boot-disk-size 240 \
   --project=${PROJECT}
    ```
9. Put the input file in the bucket:
    ```
   gcloud storage cp </path/to/dataset.csv> gs://${BUCKET_NAME}/input/input.csv
   ```
10. Build the project:
    ```
    sbt clean package
    ```
11. Put the project jar in the bucket:
    ```
    gcloud storage cp ./target/scala-2.12/copurchaseanalysis_2.12-0.1.0-SNAPSHOT.jar gs://${BUCKET_NAME}/scala/project.jar
    ```
12. Submit a job:
    ```
    gcloud dataproc jobs submit spark --cluster=${CLUSTER} \
     --class=Main \
     --jars=gs://${BUCKET_NAME}/scala/project.jar \
     --region=${REGION} \
     -- gs://${BUCKET_NAME}/input/input.csv gs://${BUCKET_NAME}/output/ <solution-id> <number-of-nodes>
    ```
    You can select any available solution, the best one is number 5.  
13. Delete the cluster:
    ```
    gcloud dataproc clusters delete ${CLUSTER} --region=${REGION} --project=${PROJECT}
    ```