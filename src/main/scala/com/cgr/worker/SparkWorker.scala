package com.cgr.worker

import org.apache.spark.sql.SparkSession

/**
  * Created by guorui.chen on 2017/4/21.
  */
trait SparkWorker {

  val warehouseLocation = "file:${system:user.dir}/spark-warehouse"

  val spark:SparkSession = SparkSession
    .builder()
    .appName("RealTimeFeatureJob")
    .config("spark.sql.warehouse.dir", warehouseLocation)
    .enableHiveSupport()
    .getOrCreate()

  implicit val sc = spark.sparkContext
  sc.setLogLevel("ERROR")

  def process()

  def run(): Unit ={
    process()
    stop()
  }

  def stop(): Unit ={
    spark.stop()
  }

}
