package com.cgr.bigdata.domain

import org.apache.spark.rdd.RDD

/**
  * Created by guorui.chen on 2017/4/24.
  */
trait BaseDataVO extends java.io.Serializable{

  def getData[A]():A

}
