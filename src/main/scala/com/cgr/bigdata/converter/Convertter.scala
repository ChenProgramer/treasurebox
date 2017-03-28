package com.cgr.bigdata.converter

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Created by guorui.chen on 2017/3/28.
  */
trait Convertter {

  def convertter(spark:SparkSession,trainRDD: RDD[LabeledPoint],testRDD: RDD[LabeledPoint]): (RDD[LabeledPoint],RDD[LabeledPoint])

}
