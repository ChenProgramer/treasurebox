package com.cgr.bigdata.domain

import scala.beans.BeanProperty

/**
  * Created by guorui.chen on 2017/3/28.
  */
class BigDataVO {

  @BeanProperty var key:String = null

  @BeanProperty var doubles:List[Double] = List[Double]()

  @BeanProperty var strings:List[String] = List[String]()

  @BeanProperty var integers:List[Integer] = List[Integer]()
}
