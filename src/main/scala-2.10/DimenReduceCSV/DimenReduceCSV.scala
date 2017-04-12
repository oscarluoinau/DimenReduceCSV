package DimenReduceCSV

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DoubleType

/**
  * Created by luo024 on 20/03/2017.
  */

object DimenReduceCSV {
  def main(args: Array[String]) {
    val logFile = args(0)
    val percent = args(1)
    val outFile = args(2)
    val conf = new SparkConf().setAppName("DimenReduceCSV")
    val sc = new SparkContext(conf)
    //val logData = sc.textFile(logFile, 2).cache()
    //val numAs = logData.filter(line => line.contains("a")).count()
    //val numBs = logData.filter(line => line.contains("b")).count()
    //println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    // this is used to implicitly convert an RDD to a DataFrame.
    import sqlContext.implicits._
    val df = sqlContext.read.format("com.databricks.spark.csv").option("header","true").option("inferSchema", "true").load(logFile)
    //df.select(df("cell"), df("0610007P14Rik")).show(10)
    //df.printSchema()

    val genes = df.columns.drop(1)
    //val sums = df.select("0610007P14Rik").rdd.map(r => r(0).asInstanceOf[Double]).collect().sum

    val i = 0 to 19
    //val colSums = genes.map(x => df.select(col(x).cast(DoubleType)).rdd.map(r => r(0).asInstanceOf[Double]).collect().sum)
    val colSums = i.map(x => df.select(col(genes(x)).cast(DoubleType)).rdd.map(r => r(0).asInstanceOf[Double]).collect().sum)

    //val num = df.select(col("0610025J13Rik").cast(DoubleType)).rdd.map(r => r(0).asInstanceOf[Double]).collect()
    val res = new percentile
    //println(res.computePercentile(num, percent.toDouble))
    println(res.computePercentile(colSums, percent.toDouble))

    val cutoff = res.computePercentile(colSums, percent.toDouble)

    for(i <- 0 to 19){
      println(genes(i), colSums(i))
    }

    val index = colSums.zipWithIndex.filter(_._1 > cutoff).map(_._2)
    println(index)
    println(index.map(x => genes(x)))

    val retainedGenes = index.map(x => genes(x))
    val retainedCols = Array("cell") ++ retainedGenes
    val retainedDF = df.select(retainedCols.map(c => col(c)): _*)

    retainedDF.coalesce(1).write.format("com.databricks.spark.csv").option("header", "true").save(outFile)
  }
}
