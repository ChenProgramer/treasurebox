package com.cgr.utils.datasetutils

import com.cgr.bigdata.domain.BigDataVO
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{ArrayType, DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.mutable

/**
  * Created by guorui.chen on 2017/3/29.
  */
object TreasureBoxDataSetUtils {

  /**
    * 将labeledpoint的RDD转换为xgoost使用的dataframe
    */
  def convertRDDToDataFrame(spark:SparkSession,rdd: RDD[LabeledPoint]): DataFrame = {
    val list = List[StructField](StructField("label",DoubleType,true),StructField("features0",ArrayType(DoubleType,false),true))
    val schemaScore = StructType(list)
    val trainrow =rdd.map{
      case(labelPoint)=>
        Row(labelPoint.label,labelPoint.features.toArray)
    }
    val dataframe = spark.createDataFrame(trainrow,schemaScore)
    dataframe.withColumn("features",toVec4(dataframe("features0")))
  }

  val toVec4    = udf[Vector, mutable.WrappedArray[Double]] { (a) =>
    Vectors.dense(a.toArray)
  }

  def computeMin[A](rdd:RDD[A]):A={
    val sortRDD = rdd.sortBy(f => f.toString.toDouble)
    sortRDD.collect().apply(0).asInstanceOf[A]
  }


  //根据数据集计算最大值
  def computeMax[A](rdd:RDD[A]):A={
    val sortRDD = rdd.sortBy(f => f.toString.toDouble).zipWithIndex()
    val count = sortRDD.count()
    sortRDD.filter(x => x._2 + 1L == count).collect().apply(0)._1.asInstanceOf[A]
  }

  //根据数据集计算阈值
  def computeThreshold[A](rdd:RDD[A],alpha:Double):A={
    val sortRDD = rdd.sortBy(x => x.toString.toDouble).zipWithIndex()
    val count = sortRDD.count()
    val rank = (count * alpha).toLong
    sortRDD.filter(x => x._2 + 1L == rank).collect().apply(0)._1.asInstanceOf[A]
  }

  //将RDD存储到hive里面
  def dataStorageHive[A](rdd:RDD[A],fields:Array[String],table:String):Unit={
    rdd.foreach(println)
  }

  //将RDD存储到hive里面
  def dataStorageHbase[A](rdd:RDD[A],fields:Array[String],table:String):Unit={

  }

}
