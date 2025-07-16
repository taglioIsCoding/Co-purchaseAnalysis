# Co-purchase Analysis
Repository of the code produced for the Scalable and Cloud Programming course. 

## Project Structure 
- [solutions](./src/main/scala/solutions) folder: all the explored solutions 
- [resources](./src/main/resources) folder: reduced version of the dataset used to compare locally the solutions 
- [data_analysis](./data_analysis/data-analysis.ipynb) folder: python scripts used to do data analysis

## Solutions 
The program can be used with 4 parameters:
- the path of the input file 
- the path of the output folder 
- the solution code 
- the number of nodes

The only solution which efficiently supports the number of node param is BestSolutionWithPartitions. 
All other solutions will ignore the param and so it can be omitted. 
Each explored solution has a solution-id that can be used as a parameter when running the project.
Available solutions:
0. FirstSolution
1. GroupByKey
2. NewPairsMapping
3. MergeTwoStages
4. MergeTwoStagesNoFilter
5. BestSolutionWithPartitions

## Run on DataProc
1. Create a new project on Google Cloud Platform 
2. Enable DapaProc and the APIs
3. Create and download the keys 
4. Export usefull variables 
    ```
    export GOOGLE_APPLICATION_CREDENTIALS="/path/to/key.json" \
    export PROJECT=<project-id> \
    export BUCKET_NAME=<bucket-name> \
    export CLUSTER=<cluster-name> \
    export REGION="us-central1"
   ```
5. Clone this repository <br>
   ```git clone https://github.com/taglioIsCoding/Co-purchaseAnalysis/```
6. Initialize the Google cloud project  <br>
   ```gcloud init```
7. Create the bucket <br>
    ```gcloud storage buckets create gs://${BUCKET_NAME} --location=${REGION}```
8. Create the cluster <br>
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
9. Put the input file in the bucket <br>
    ```gcloud storage cp </path/to/dataset.csv> gs://${BUCKET_NAME}/input/input.csv```
10. Build the project <br>
    ```
    sbt clean package
    ```
11. Put the project jar in the bucket <br>
    ```
    gcloud storage cp ./target/scala-2.12/copurchaseanalysis_2.12-0.1.0-SNAPSHOT.jar gs://${BUCKET_NAME}/scala/project.jar
    ```
12. Submit a job 
    ```
    gcloud dataproc jobs submit spark --cluster=${CLUSTER} \
     --class=Main \
     --jars=gs://${BUCKET_NAME}/scala/project.jar \
     --region=${REGION} \
     -- gs://${BUCKET_NAME}/input/input.csv gs://${BUCKET_NAME}/output/ 5 3
    ```
    You can select every solution available, the best one is the number 5.  
13. Delete the cluster <br>
    ```gcloud dataproc clusters delete ${CLUSTER} --region=${REGION} --project=${PROJECT}```