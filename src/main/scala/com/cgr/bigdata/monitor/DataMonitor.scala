package com.cgr.bigdata.monitor

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Created by guorui.chen on 2017/3/29.
  */
trait DataMonitor {

  def monitor(rdd:RDD[(Double,Double)],needCount:Boolean):Unit

  def setSparksession(spark: SparkSession):Unit

}
