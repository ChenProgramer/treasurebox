package com.cgr.bigdata.computer

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

/**
  * Created by guorui.chen on 2017/3/28.
  */
trait Computer {

  def compute(trainRDD:RDD[LabeledPoint],testRDD:RDD[LabeledPoint]):Double

}
