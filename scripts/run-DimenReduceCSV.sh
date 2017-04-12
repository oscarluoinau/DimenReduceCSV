#!/bin/bash


input_file="hdfs://ip-172-31-14-34.ap-southeast-2.compute.internal/user/luo024/variantSparkTestData/single-cell_RNA-Seq/mESC_samples_expression_log.csv"
percentile_cutoff=50
output=/home/luo024/single-cell_RNA-Seq/mESC_samples_expression_log_reduce50.csv

spark-submit --packages com.databricks:spark-csv_2.10:1.5.0 \
      --class DimenReduceCSV.DimenReduceCSV \
      --master yarn-client \
      --num-executors 10 \
      --executor-memory 4G \
      --executor-cores 4 \
      --driver-memory 2G \
      /home/luo024/DimenReduceCSV/target/scala-2.10/dimenreducecsv_2.10-1.0.jar \
      $input_file \
      $percentile_cutoff \
      $output >test.out

