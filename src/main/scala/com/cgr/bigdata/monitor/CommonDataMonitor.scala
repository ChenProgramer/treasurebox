package com.cgr.bigdata.monitor

import org.apache.spark.rdd.RDD

/**
  * Created by guorui.chen on 2017/3/29.
  */
class CommonDataMonitor extends DataMonitor{

  def aucMonitor(rdd:RDD[(Double,Double)]):Unit={
      val positiveRDD = rdd.filter(x => x._2 == 1.0)
      val negativeRDD = rdd.filter(x => x._2 == 0.0)
  }

  override def monitor(): Unit = {

  }
}
