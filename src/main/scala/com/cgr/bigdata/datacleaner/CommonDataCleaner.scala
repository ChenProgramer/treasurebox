package com.cgr.bigdata.datacleaner
import com.cgr.bigdata.domain.BigDataVO
import org.apache.spark.rdd.RDD

/**
  * Created by guorui.chen on 2017/3/29.
  */
class CommonDataCleaner extends DataCleaner{

  override def cleanData(rdd: RDD[BigDataVO]): RDD[BigDataVO] = {
    rdd.filter{
      case(bigdataVO) =>
        bigdataVO.getDoubles.contains(0.8)
    }
  }
}
