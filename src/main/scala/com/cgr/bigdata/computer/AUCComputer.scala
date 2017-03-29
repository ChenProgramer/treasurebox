package com.cgr.bigdata.computer

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS, LogisticRegressionWithSGD}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

/**
  * Created by guorui.chen on 2017/3/28.
  */
class AUCComputer extends Computer with java.io.Serializable{

  var model:LogisticRegressionModel = null

  override def compute(trainRDD: RDD[LabeledPoint], testRDD: RDD[LabeledPoint]): Double = {
    model = new LogisticRegressionWithLBFGS().setNumClasses(10).run(trainRDD)
    val predictionAndLabels = testRDD.map { case LabeledPoint(label, features) =>
      val prediction = model.predict(features)
      (prediction, label)
    }
    val metrics = new MulticlassMetrics(predictionAndLabels)
    metrics.accuracy
  }
}
