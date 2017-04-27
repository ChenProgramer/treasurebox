package com.cgr.bigdata.processor

import com.cgr.bigdata.computer.Computer
import com.cgr.bigdata.converter.Convertter
import com.cgr.bigdata.datacutter.DataCutter
import com.cgr.bigdata.dataloader.AUCDataLoader
import org.apache.spark.sql.SparkSession

/**
  * Created by guorui.chen on 2017/3/27.
  */
class AUCProcessor(dataLoader:AUCDataLoader,dataCutter:DataCutter,convertter:Convertter,computer:Computer) {

  def computeAUCValue(): Unit={

  }

  def run(): Unit ={
    val warehouseLocation = "file:${system:user.dir}/spark-warehouse"
    val spark = SparkSession
      .builder()
      .appName("RealTimeFeatureJob_WithLogic")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()
    implicit val sc = spark.sparkContext
    spark.sparkContext.setLogLevel("ERROR")
    val rdd_labelPoint = dataLoader.initDataSet(spark)
    val trainDataAndTestData = dataCutter.cutData(rdd_labelPoint,false)
    val trainDataAndTestDataConvertted = convertter.convertter(spark,trainDataAndTestData._1,trainDataAndTestData._2)
    val rs = computer.compute(trainDataAndTestDataConvertted._1,trainDataAndTestDataConvertted._1)
    println(rs)
  }

}
