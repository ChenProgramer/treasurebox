package com.cgr.utils.modelutils

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.spark.mllib.classification.LogisticRegressionModel
import org.apache.spark.sql.SparkSession

/**
  * Created by guorui.chen on 2017/4/26.
  */
object TreasureBoxModelUtils {

  def deleteTargetFile(savePath:String,spark:SparkSession,zookeeperUrl:String):Unit={
    val file1 = savePath + "data";
    val file2 = savePath + "metadata";
    val configuration = HBaseConfiguration.create();
    configuration.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
    configuration.set("hbase.zookeeper.quorum", zookeeperUrl);
    val hdfs = FileSystem.get(configuration)
    var path = new Path(file1)
    if (hdfs.exists(path)){
      hdfs.delete(path,true)
    }
    path = new Path(file2)
    if (hdfs.exists(path)){
      hdfs.delete(path,true)
    }
  }

  def logicModelSave(model:LogisticRegressionModel,savePath:String,spark:SparkSession,zookeeperUrl:String):Unit = {
    deleteTargetFile(savePath,spark,zookeeperUrl)
    model.save(spark.sparkContext,savePath)
  }

}
