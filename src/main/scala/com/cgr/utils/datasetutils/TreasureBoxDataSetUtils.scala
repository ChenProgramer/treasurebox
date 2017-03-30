package com.cgr.utils.datasetutils

import com.cgr.bigdata.domain.BigDataVO
import org.apache.spark.rdd.RDD

/**
  * Created by guorui.chen on 2017/3/29.
  */
object TreasureBoxDataSetUtils {

  //根据数据集计算阈值
  def computeThreshold[A](rdd:RDD[A],alpha:Double):A={
    val sortRDD = rdd.sortBy(f => f).zipWithIndex()
    val count = sortRDD.count()
    val rank = (count * alpha).toLong
    sortRDD.filter(x => x._2 + 1L == rank).collect().apply(0)._1
  }

  //将RDD存储到hive里面
  def dataStorageHive[A](rdd:RDD[A],fields:Array[String],table:String):Unit={

  }

  //将RDD存储到hive里面
  def dataStorageHbase[A](rdd:RDD[A],fields:Array[String],table:String):Unit={

  }

}
