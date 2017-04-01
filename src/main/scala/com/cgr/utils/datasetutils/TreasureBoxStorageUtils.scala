package com.cgr.utils.datasetutils

import com.cgr.consts.ConventionConsts
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._

/**
  * Created by guorui.chen on 2017/4/1.
  */
class TreasureBoxStorageUtils {

  def aucStorageUtils(spark:SparkSession,rdd:RDD[(Double,Double)],taskName:String):Unit={
    val list = List[StructField](StructField("score",StringType,true),StructField("label",StringType,true))
    val schemas = StructType(list)
    val saveRDD =rdd.map{
      case(score,label)=>
        Row(score.toString,label.toString)
    }
    spark.createDataFrame(saveRDD,schemas).createOrReplaceTempView("tmp_treasureboxstorageutils")
    spark.sql("insert overwrite table " + ConventionConsts.HIVE_AUC_MONITOR_OUTPUT_TABLE + " partition(task='" + taskName + "') select score,label from tmp_treasureboxstorageutils")
  }

}
