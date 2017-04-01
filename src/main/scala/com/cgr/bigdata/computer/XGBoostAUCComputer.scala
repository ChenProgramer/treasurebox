package com.cgr.bigdata.computer

import com.cgr.bigdata.debuger.DataDebuger
import com.cgr.bigdata.monitor.{CommonDataMonitor, DataMonitor}
import com.cgr.utils.datasetutils.TreasureBoxDataSetUtils
import ml.dmlc.xgboost4j.scala.spark.XGBoost
import org.apache.spark.mllib.evaluation.{BinaryClassificationMetrics, MulticlassMetrics}
import org.apache.spark.mllib.regression.{LabeledPoint => MLLabeledPoint}
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.beans.BeanProperty

/**
  * Created by guorui.chen on 2017/3/31.
  */
class XGBoostAUCComputer extends Computer{

  @BeanProperty var spark:SparkSession = null

  @BeanProperty var monitor:DataMonitor = null

  override def compute(trainRDD: RDD[MLLabeledPoint], testRDD: RDD[MLLabeledPoint]): Double = {
    require()
    val xgboostTrain = TreasureBoxDataSetUtils.convertRDDToDataFrame(spark,trainRDD)
    val model = XGBoost.trainWithDataFrame(xgboostTrain, paramMap,numRound, numWorkers, useExternalMemory = true)
    val xgboostTest = TreasureBoxDataSetUtils.convertRDDToDataFrame(spark,testRDD)
    val testPred = model.transform(xgboostTest).select("probabilities","label")
    val predictionAndLabels = testPred.rdd.map {
      case(row) =>
        val pred = row.get(0).toString.replace("[","").replace("]","").split(",").map(x => x.toDouble).apply(1).toDouble
        val label = row.get(1).toString.toDouble
        (pred.formatted("%.10f").toDouble,label.formatted("%.10f").toDouble)
    }
    val metrics = new BinaryClassificationMetrics(predictionAndLabels)
    monitor.monitor(predictionAndLabels,true)
    metrics.areaUnderROC()
  }

  private def require(): Unit ={
    if(monitor == null){
      monitor = new CommonDataMonitor()
      monitor.setSparksession(spark)
    }
    if(spark == null)
      throw new IllegalArgumentException("[TreasureBox] [XGBoostAUCComputer] : giving SparkSession is null !")
  }

  @BeanProperty var numRound = 40
  @BeanProperty var numWorkers = 4
  @BeanProperty var max_depth = 6
  @BeanProperty var colsample_bytree = 0.8
  @BeanProperty var colsample_bylevel = 0.9
  @BeanProperty var paramMap = List(
    //    "eta" -> 0.023f,
    "max_depth" -> max_depth,
    //      "subsample" -> 1.0,
    "colsample_bytree" -> colsample_bytree,
    "colsample_bylevel" -> colsample_bylevel,
    "eval_metric" -> "auc",
    "objective" -> "binary:logistic").toMap
}
