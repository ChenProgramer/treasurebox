package com.cgr.bigdata.dataloader
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import redis.clients.jedis.Jedis

/**
  * Created by guorui.chen on 2017/3/29.
  */
class RedisDataLoader extends DataLoader{

  var jedis:Jedis = null

  def init(jedis:Jedis): Unit ={
      this.jedis = jedis
  }

  override def initDataSet(spark: SparkSession): RDD[LabeledPoint] = {
      null
  }
}
