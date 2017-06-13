package com.cgr.bigdata.dataloader

import com.cgr.bigdata.domain.BaseDataVO
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Created by guorui.chen on 2017/4/24.
  */
trait BaseDataLoader extends java.io.Serializable{

  def safe[S, T](f: S => T): S => Either[T, (S, Exception)] = {
    new Function[S, Either[T, (S, Exception)]] with Serializable {
      def apply(s: S): Either[T, (S, Exception)] = {
        try {
          Left(f(s))
        } catch {
          case e: Exception => Right((s, e))
        }
      }
    }
  }

  def getBaseData(spark:SparkSession):RDD[BaseDataVO]

}
