package com.cgr.bigdata.computer

import com.cgr.bigdata.debuger.DataDebuger
import com.cgr.bigdata.monitor.DataMonitor
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
//    println("-----------得分结果分析  start-----------")
//    val zhengRDD = predictionAndLabels.filter(x => x._2 == 1.0)
//    val fuRDD = predictionAndLabels.filter(x => x._2 == 0.0)
//    zhengRDD.cache()
//    fuRDD.cache()
//    val zhengRDDd7 = zhengRDD.filter(x => x._1 > 0.7)
//    val zhengRDDd3 = zhengRDD.filter(x => x._1 > 0.3 && x._1 <= 0.7)
//    val zhengRDDd1 = zhengRDD.filter(x => x._1 > 0.1 && x._1 <= 0.3)
//    val zhengRDDd0 = zhengRDD.filter(x => x._1 <= 0.3)
//    val fuRDD7 = fuRDD.filter(x => x._1 > 0.7)
//    val fuRDD3 = fuRDD.filter(x => x._1 > 0.3 && x._1 <= 0.7)
//    val fuRDD1 = fuRDD.filter(x => x._1 > 0.1 && x._1 <= 0.3)
//    val fuRDD0 = fuRDD.filter(x => x._1 <= 0.1)
//    val countZAll = zhengRDD.count()
//    val countZ7 = zhengRDDd7.count()
//    val countZ3 = zhengRDDd3.count()
//    val countZ1 = zhengRDDd1.count()
//    val countZ0 = zhengRDDd0.count()
//    val countFAll = fuRDD.count()
//    val countF7 = fuRDD7.count()
//    val countF3 = fuRDD3.count()
//    val countF1 = fuRDD1.count()
//    val countF0 = fuRDD0.count()
//    val zP = zhengRDD.map(x => x._1).reduce((x,y) => x+y) / countZAll
//    val fp = fuRDD.map(x => x._1).reduce((x,y) => x+y) / countFAll
//    println("正样本数量为 : "+countZAll)
//    println("正样本得分 大于0.7 数量为 : "+countZ7)
//    println("正样本得分 大于0.3 并 小于等于0.7 数量为 : "+countZ3)
//    println("正样本得分 大于0.1 并 小于等于0.3 数量为 : "+countZ1)
//    println("正样本得分 小于等于0.1 数量为 : "+countZ0)
//    println("负样本数量为 : "+countFAll)
//    println("负样本得分 大于0.7 数量为 : "+countF7)
//    println("负样本得分 大于0.3 并 小于等于0.7 数量为 : "+countF3)
//    println("负样本得分 大于0.1 并 小于等于0.3 数量为 : "+countF1)
//    println("负样本得分 小于等于0.1 数量为 : "+countF0)
//    println("-----------得分结果分析  end-----------")
    predictionAndLabels.foreach(println)
    val metrics = new BinaryClassificationMetrics(predictionAndLabels)
    metrics.areaUnderROC()
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
