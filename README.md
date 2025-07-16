# Co-purchase Analysis
Repository of the code produced for the Scalable and Cloud Programming course. 

## Run on DataProc
1. Create a new project on Google Cloud Platform 
2. Enable DapaProc and the APIs
3. Create and download the keys 
4. Export usefull variables 
    ```
    export GOOGLE_APPLICATION_CREDENTIALS="/Path/to/key.json" \
    export PROJECT=<project-id> \
    export BUCKET_NAME=<bucket-name> \
    export CLUSTER=<cluster-name> \
    export REGION="us-central1" \
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
    ```gcloud storage cp order_products.csv gs://${BUCKET_NAME}/input/input.csv```
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
    gcloud dataproc jobs submit spark \ 
    --cluster=${CLUSTER} \
    --class=Main \
    --jars=gs://${BUCKET_NAME}/scala/project.jar \
    --region=${REGION} \
    -- gs://${BUCKET_NAME}/input/input.csv gs://${BUCKET_NAME}/output/ <solution-code> 
    ```
    You can select every solution available, the best one is the number 4.  
13. Delete the cluster <br>
    ```gcloud dataproc clusters delete ${CLUSTER} --region=${REGION} --project=${PROJECT}```