package com.cgr.consts

/**
  * Created by guorui.chen on 2017/3/29.
  */
object TimeReferenseConsts {

  val str_day_1 = "yyyyMMdd"

  val str_second_1 = "yyyyMMdd HH:mm:ss"

  val str_msec_1 = "yyyyMMdd HH:mm:ss:SSS"

  val str_day_2 = "yyyy-MM-dd"

  val str_second_2 = "yyyy-MM-dd HH:mm:ss"

  val str_msec_2 = "yyyy-MM-dd HH:mm:ss:SSS"

  val str_day_3 = "yyyy年MM月dd日"

  val str_second_3 = "yyyy年MM月dd日 HH时mm分ss秒"

  val str_msec_3 = "yyyy年MM月dd日 HH时mm分ss秒SSS"

  val str_day_4 = "yyyy.MM.dd"

  val str_second_4 = "yyyy.MM.dd HH:mm:ss"

  val str_msec_4 = "yyyy.MM.dd HH:mm:ss:SSS"

  val str_day_5 = "yyyy/MM/dd"

  val str_second_5 = "yyyy/MM/dd HH:mm:ss"

  val str_msec_5 = "yyyy/MM/dd HH:mm:ss:SSS"

  val strList = List(TimeReferenseConsts.str_day_1,
    TimeReferenseConsts.str_day_2,
    TimeReferenseConsts.str_day_3,
    TimeReferenseConsts.str_day_4,
    TimeReferenseConsts.str_day_4,
    TimeReferenseConsts.str_day_5,
    TimeReferenseConsts.str_second_1,
    TimeReferenseConsts.str_second_2,
    TimeReferenseConsts.str_second_3,
    TimeReferenseConsts.str_second_4,
    TimeReferenseConsts.str_second_5,
    TimeReferenseConsts.str_msec_1,
    TimeReferenseConsts.str_msec_2,
    TimeReferenseConsts.str_msec_3,
    TimeReferenseConsts.str_msec_4,
    TimeReferenseConsts.str_msec_5
  )

  val patternMap = Map[String,String](this.str_day_1 -> "^\\d{4}\\d{2}\\d{2}$",
    this.str_day_2 -> "^\\d{4}-\\d{2}-\\d{2}$",
    this.str_day_3 -> "^\\d{4}年\\d{2}月\\d{2}日$",
    this.str_day_4 -> "^\\d{4}\\.\\d{2}\\.\\d{2}$",
    this.str_day_5 -> "^\\d{4}/\\d{2}/\\d{2}$",
    this.str_second_1 -> "^\\d{4}\\d{2}\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$",
    this.str_second_2 -> "^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$",
    this.str_second_3 -> "^\\d{4}年\\d{2}月\\d{2}\\s\\d{2}时\\d{2}分\\d{2}秒$",
    this.str_second_4 -> "^\\d{4}\\.\\d{2}\\.\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$",
    this.str_second_5 -> "^\\d{4}/\\d{2}/\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$",
    this.str_msec_1 -> "^\\d{4}\\d{2}\\d{2}\\s\\d{2}:\\d{2}:\\d{2}:\\d{3}$",
    this.str_msec_2 -> "^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}:\\d{3}$",
    this.str_msec_3 -> "^\\d{4}年\\d{2}月\\d{2}\\s\\d{2}时\\d{2}分\\d{2}秒:\\d{3}$",
    this.str_msec_4 -> "^\\d{4}\\.\\d{2}\\.\\d{2}\\s\\d{2}:\\d{2}:\\d{2}:\\d{3}$",
    this.str_msec_5 -> "^\\d{4}/\\d{2}/\\d{2}\\s\\d{2}:\\d{2}:\\d{2}:\\d{3}$"
  )

}
