package com.cgr.utils.datasetutils

import com.cgr.consts.ConventionConsts
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._

/**
  * Created by guorui.chen on 2017/4/1.
  */
object TreasureBoxStorageUtils {

  def aucMonitorStorage(spark:SparkSession,rdd:RDD[(Double,Double)],taskName:String):Unit={
    val list = List[StructField](StructField("score",StringType,true),StructField("label",StringType,true))
    val schemas = StructType(list)
    val saveRDD =rdd.map{
      case(score,label)=>
        Row(score.toString,label.toString)
    }
    spark.createDataFrame(saveRDD,schemas).createOrReplaceTempView("tmp_treasureboxstorageutils_aucmonitorstorage")
    spark.sql("insert overwrite table " + ConventionConsts.HIVE_AUC_MONITOR_OUTPUT_TABLE + " partition(task='" + taskName + "') select score,label from tmp_treasureboxstorageutils_aucmonitorstorage")
  }

  def aucCountStorage(spark:SparkSession,rdd:RDD[(String,String)],taskName:String):Unit={
    val list = List[StructField](StructField("field",StringType,true),StructField("value",StringType,true))
    val schemas = StructType(list)
    val saveRDD =rdd.map{
      case(score,label)=>
        Row(score.toString,label.toString)
    }
    spark.createDataFrame(saveRDD,schemas).createOrReplaceTempView("tmp_treasureboxstorageutils_auccountstorage")
    spark.sql("insert overwrite table " + ConventionConsts.HIVE_AUC_COUNT_OUTPUT_TABLE + " partition(task='" + taskName + "') select field,value from tmp_treasureboxstorageutils_auccountstorage")
  }

}
