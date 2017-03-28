package com.cgr.bigdata.datacutter

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

/**
  * Created by guorui.chen on 2017/3/27.
  */
trait DataCutter {

  def cutData(rdd:RDD[LabeledPoint],baseOnSample:Boolean):(RDD[LabeledPoint],RDD[LabeledPoint])

}
