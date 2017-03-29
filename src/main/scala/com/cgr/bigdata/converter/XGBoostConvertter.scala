package com.cgr.bigdata.converter

import ml.dmlc.xgboost4j.scala.spark.{XGBoost, XGBoostModel}
import org.apache.spark
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types.{ArrayType, DoubleType, StructField, StructType}
import org.apache.spark.mllib.linalg.{Vector => MLVector, Vectors => MLVectors}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by guorui.chen on 2017/3/28.
  */
class XGBoostConvertter() extends Convertter with java.io.Serializable{

  var model: XGBoostModel = null

  var trainRDD:RDD[LabeledPoint] = null

  var testRDD:RDD[LabeledPoint] = null

  var leaves:List[List[Double]] = null

  override def convertter(spark:SparkSession,trainRDD: RDD[LabeledPoint],testRDD: RDD[LabeledPoint]): (RDD[LabeledPoint],RDD[LabeledPoint]) = {
    init(spark,trainRDD)
    val dataFrame = convertRDDToDataFrame(spark,testRDD)
    val prediction = model.setExternalMemory(true).transformLeaf(dataFrame)
    this.testRDD = getleaves(prediction)
    (trainRDD,testRDD)
  }

  def init(spark:SparkSession,rdd: RDD[LabeledPoint]):Unit={
    val dataFrame = convertRDDToDataFrame(spark,rdd)
    model = XGBoost.trainWithDataFrame(dataFrame, paramMap,numRound, numWorkers, useExternalMemory = true)
    train(spark,rdd)
  }

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

  def train(spark:SparkSession,rdd: RDD[LabeledPoint]): Unit = {
    val dataFrame = convertRDDToDataFrame(spark,rdd)
    var prediction = model.setExternalMemory(true).transformLeaf(dataFrame)
    for(i <- 0 to numRound-1){
      prediction = prediction.withColumn("tree"+i,getcolumn(prediction("predLeaf"),lit(i)))
    }
    var tempTrees = ArrayBuffer[List[Double]]()
    for(i <- 0 to numRound-1){
      val treelist = prediction.select("tree"+i).rdd.map{
        case(vals) =>
          vals.getDouble(0)
      }.aggregate(ArrayBuffer[Double]())(unitArrayBufferElement,unitArrayBuffers).distinct
      if(i ==0 && treelist.size ==1 && treelist(0) == 0.0) {
        throw new Exception("someThing wrong in the train throw exception")
      }
      tempTrees.append(treelist.toList)
    }
    leaves = tempTrees.toList
    tempTrees.toList
  }

  def getleaves(predictions:DataFrame):RDD[LabeledPoint]={
    val result = predictions.withColumn("vector",getvector(predictions("predLeaf")))
    result.select("vector","label").rdd.map{
      case(vals)=>
        val l = vals.getAs("label").toString.toDouble
        val arrays = vals.getList[Double](0)
        val s= arrays.toArray().map(x => x.toString.toDouble)
        val positiveTransForm = MLVectors.dense(s)
        LabeledPoint(l,positiveTransForm)
    }
  }

  val numRound = 40
  val numWorkers = 4
  val max_depth = 6
  val colsample_bytree = 0.8
  val colsample_bylevel = 0.9
  val useExternalMemory = true

  val paramMap = List(
    //    "eta" -> 0.023f,
    "max_depth" -> max_depth,
    //      "subsample" -> 1.0,
    "colsample_bytree" -> colsample_bytree,
    "colsample_bylevel" -> colsample_bylevel,
    "eval_metric" -> "auc",
    "objective" -> "binary:logistic").toMap

  val getcolumn    = udf[Double, mutable.WrappedArray[Float],Int] { (a,b) =>
    a(b).toDouble
  }

  val unitArrayBufferElement = (x:ArrayBuffer[Double],y:Double)=> {
    x.append(y)
    x
  }

  val unitArrayBuffers = (x:ArrayBuffer[Double],y:ArrayBuffer[Double])=> {
    x.++(y)
  }

  val getvector    = udf[Array[Double], mutable.WrappedArray[Float]] { (a) =>
    var r2 = List[Double]()
    for(i <- 0 to numRound-1){
      val v = leaves(i).map{
        case(v)=>
          if(a(i) == v) 1.0
          else 0.0
      }
      r2 = r2 ++ v
    }
    r2.toArray
  }

  val toVec4    = udf[Vector, mutable.WrappedArray[Double]] { (a) =>
    Vectors.dense(a.toArray)
  }

}
