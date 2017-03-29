package com.cgr.bigdata.dataloader

import com.cgr.bigdata.datacleaner.DataCleaner
import com.cgr.bigdata.domain.BigDataVO
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.mllib.linalg.{Vector => MLVector, Vectors => MLVectors}

import scala.beans.BeanProperty

/**
  * Created by guorui.chen on 2017/3/27.
  */
class HiveDataLoader(tableName:String,contidionMap:Map[String,String])  extends DataLoader with java.io.Serializable{

  @BeanProperty var dataCleaner:DataCleaner = null

  private val templateSQL = "select vecsstr,y from "

  override def initDataSet(spark: SparkSession): RDD[LabeledPoint] = {
    val conditionStr = analysisContidionMap(contidionMap)
    val sql = templateSQL + tableName + conditionStr
    val dataFrame = spark.sql(sql)
    dataFrame.rdd.map{
      case(row)=>
        val label = row.getString(1).toDouble
        val features = row.getString(0).split(",").map(x => x.toDouble)
        val vector = MLVectors.dense(features)
        val result = new LabeledPoint(label,vector)
        result
    }
  }

  def analysisContidionMap(contidionMap: Map[String, String]):String = {
    val result = if(contidionMap.size > 0 ) new StringBuilder(" where ") else new StringBuilder("")
    var index = 1
    contidionMap.foreach(x =>
      if(index == contidionMap.size){
        result.append(x._1 + " = " + "'" +x._2 + "'")
        index += 1
      }else{
        result.append(x._1 + " = " + "'" +x._2 + "'").append(" and ")
        index += 1
      })
    result.toString()
  }

}
