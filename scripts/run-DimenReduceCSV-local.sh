#!/bin/bash

input_file="hdfs://ip-172-31-14-34.ap-southeast-2.compute.internal/user/luo024/variantSparkTestData/single-cell_RNA-Seq/test.csv"
percentile_cutoff=50
output="hdfs://ip-172-31-14-34.ap-southeast-2.compute.internal/user/luo024/variantSparkTestData/single-cell_RNA-Seq/test_out"

spark-submit --packages com.databricks:spark-csv_2.10:1.5.0 \
      --class DimenReduceCSV.DimenReduceCSV \
      --master local[8] \
      /home/luo024/DimenReduceCSV/target/scala-2.10/dimenreducecsv_2.10-1.0.jar \
      $input_file \
      $percentile_cutoff \
      $output >test.out

