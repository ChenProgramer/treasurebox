package com.cgr.bigdata.datacutter

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

/**
  * Created by guorui.chen on 2017/4/6.
  */
class RatingDataCutter  extends DataCutter{
  override def cutData(rdd: RDD[LabeledPoint], baseOnSample: Boolean): (RDD[LabeledPoint], RDD[LabeledPoint]) = {
    println("get start off by the jourety")
    null
  }
}
