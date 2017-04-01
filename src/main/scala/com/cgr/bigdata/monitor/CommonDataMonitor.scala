package com.cgr.bigdata.monitor

import java.util.UUID

import com.cgr.utils.datasetutils.TreasureBoxStorageUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.beans.BeanProperty
import scala.collection.mutable.{Map => MutableMap}

/**
  * Created by guorui.chen on 2017/3/29.
  */
class CommonDataMonitor extends DataMonitor{

  @BeanProperty var spark:SparkSession = null

  private val taskUUID = "[TreasureBox] [CommonDataMonitor]" + UUID.randomUUID().toString

  override def monitor(rdd:RDD[(Double,Double)],needCount:Boolean):Unit={
    require(spark,rdd)
    TreasureBoxStorageUtils.aucMonitorStorage(spark,rdd,taskUUID)
    if(needCount != null && needCount){
      val fieldAndValue:MutableMap[String,String] = MutableMap[String,String]()
      val positiveRDD = rdd.filter(x => x._2 == 1.0)
      val negativeRDD = rdd.filter(x => x._2 == 0.0)
      for(i <- 0 to thresholds.length){
        val min = if( i == 0) 0.0 else thresholds.apply(i-1)
        val max = if( i == thresholds.length) 1.0 else thresholds.apply(i)
        val positive = positiveRDD.filter(x => x._1 > min && x._1 <= max).count()
        val negative = negativeRDD.filter(x => x._1 > min && x._1 <= max).count()
        fieldAndValue.put("测试集中正样本( label == 1.0 ) 得分 ( score ) 值大于 " + min + " 并 小于等于 "+ max +" 数量",positive.toString)
        fieldAndValue.put("测试集中负样本( label == 0.0 ) 得分 ( score ) 值大于 " + min + " 并 小于等于 "+ max +" 数量",negative.toString)
      }
      val countRDD = spark.sparkContext.parallelize[(String,String)](fieldAndValue.toList)
      TreasureBoxStorageUtils.aucCountStorage(spark,countRDD,taskUUID)
    }
  }

  def getTaskUUID():String ={
    taskUUID
  }

  def setThresholds(params:List[Double]):Unit={
    val filters = params.filter(x => x > 0.0 && x < 1.0).sortBy(x => x)
    thresholds = filters
  }

  private def require(spark: SparkSession,rdd:RDD[(Double,Double)]):Unit = {
    if (spark == null)
      throw new IllegalArgumentException("[TreasureBox] [CommonDataMonitor] : giving SparkSession is null !")
    if (rdd.count() == 0)
      throw new IllegalArgumentException("[TreasureBox] [CommonDataMonitor] : giving Score And Label RDD is empty !")
  }

  var thresholds:List[Double] = List[Double](0.1,0.3,0.5,0.7,0.9)

  override def setSparksession(spark: SparkSession): Unit = {
    this.spark = spark
  }
}
