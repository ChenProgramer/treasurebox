package com.cgr.bigdata.dataloader

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Created by guorui.chen on 2017/3/27.
  */
class HbaseDataLoader(conditionMap:Map[String,String]) extends DataLoader{

  override def initDataSet(spark: SparkSession): RDD[LabeledPoint] = {
    null
  }
}
