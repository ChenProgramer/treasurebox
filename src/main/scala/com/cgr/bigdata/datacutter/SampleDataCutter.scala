package com.cgr.bigdata.datacutter

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

/**
  * Created by guorui.chen on 2017/3/27.
  * sample : 训练集 与 测试集 比例
  * 例如 : 0.8 则 百分之八十为训练集 百分之二十为测试集
  * ratio : 正负样本比例
  * 例如 : 0.5 则 负样本为正样本的 2 倍
  */
class SampleDataCutter(sample:Double,ratio:Double) extends DataCutter{

  /**
    * 切分数据集
    */
  override def cutData(rdd:RDD[LabeledPoint],baseOnSample:Boolean):(RDD[LabeledPoint],RDD[LabeledPoint])={
    val result = baseOnSample match{
      case false => cutData_1(rdd)
      case true => cutData_2(rdd)
      case _ => cutData_1(rdd)
    }
    result
  }

  def cutData_1(rdd:RDD[LabeledPoint]):(RDD[LabeledPoint],RDD[LabeledPoint])={
    val indexRDD = rdd.zipWithIndex().map(x => (x._2,x._1))
    val positiveSample = indexRDD.sample(false,sample)
    val negativeSample = indexRDD.subtractByKey(positiveSample)
    val positiveResult = positiveSample.map(x => x._2)
    val negativeResult = negativeSample.map(x => x._2)
    (positiveResult,negativeResult)
  }

  def cutData_2(rdd:RDD[LabeledPoint]):(RDD[LabeledPoint],RDD[LabeledPoint])={
    val positiveData = rdd.filter(x => x.label == 1.0)
    val negativeData = rdd.filter(x => x.label == 0.0)
    val positiveVO = cutData_1(positiveData)
    val negativeVO = cutData_1(negativeData)
    (positiveVO._1.union(negativeVO._1),positiveVO._2.union(negativeVO._2))
  }

  /**
    * 检查正负样本数据集比例
    * @return
    */
  def checkSampleNum(rdd:RDD[LabeledPoint]):RDD[LabeledPoint]={
    val positiveData = rdd.filter(x => x.label == 1.0)
    val negativeData = rdd.filter(x => x.label == 0.0)
    val positiveCount = positiveData.count()
    val negativeCount = negativeData.count()
    val alpha = if(ratio == 0.0 || ratio.isNaN || ratio.isInfinity) 1.0 else 1 / ratio
    val negativeSampleCount = (positiveCount * alpha) / negativeCount
    negativeData.sample(false,negativeSampleCount).union(positiveData)
  }

}
