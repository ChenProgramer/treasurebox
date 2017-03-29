package com.cgr.bigdata.datacleaner

import com.cgr.bigdata.domain.BigDataVO
import org.apache.spark.rdd.RDD

/**
  * Created by guorui.chen on 2017/3/29.
  */
trait DataCleaner {

  def cleanData(rdd:RDD[BigDataVO]):RDD[BigDataVO]

}
