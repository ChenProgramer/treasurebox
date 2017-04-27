package com.cgr.bigdata.dataloader

import com.cgr.bigdata.domain.BaseDataVO
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Created by guorui.chen on 2017/4/24.
  */
trait BaseDataLoader {

  def getBaseData(spark:SparkSession):RDD[BaseDataVO]

}
