package com.cgr.utils.timeutils

import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

import com.cgr.consts.TimeReferenseConsts

/**
  * Created by guorui.chen on 2017/3/29.
  */
class TreasureBoxTimeUtils private(){

  init()


  val strList = List(TimeReferenseConsts.str_day_1,
    TimeReferenseConsts.str_day_2,
    TimeReferenseConsts.str_day_3,
    TimeReferenseConsts.str_second_1,
    TimeReferenseConsts.str_second_2,
    TimeReferenseConsts.str_second_3)

  val patternMap = Map[String,String](TimeReferenseConsts.str_day_1 -> "^\\d{4}\\d{2}\\d{2}$",
    TimeReferenseConsts.str_day_2 -> "^\\d{4}-\\d{2}-\\d{2}$",
    TimeReferenseConsts.str_day_3 -> "^\\d{4}年\\d{2}月\\d{2}日$",
    TimeReferenseConsts.str_second_1 -> "^\\d{4}\\d{2}\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$",
    TimeReferenseConsts.str_second_2 -> "^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$",
    TimeReferenseConsts.str_second_3 -> "^\\d{4}年\\d{2}月\\d{2}日\\s\\d{2}时\\d{2}分\\d{2}秒$")

  var sdfMap:Map[String,SimpleDateFormat] = null

  def init(): Unit ={
    sdfMap = strList.map(x => (x,new SimpleDateFormat(x))).toMap
  }

  def msecBetweenStr(start:String,end:String):Long = {
    require()
    val sdfsForstart = strList.filter( x=> start.matches(patternMap(x)))
    val sdfsForend = strList.filter( x=> end.matches(patternMap(x)))
    if(sdfsForstart.size == 0) throw new Exception("There it is a illlegal param:"+start)
    if(sdfsForend.size == 0) throw new Exception("There it is a illlegal param:"+end)
    val msecOfStart = sdfMap(sdfsForstart.apply(0)).parse(start).getTime
    val msecOfEnd = sdfMap(sdfsForend.apply(0)).parse(end).getTime
    msecOfEnd - msecOfStart
  }

  def moveDay(originDay:String,days:Integer):String={
    require()
    ""
  }

  def require():Unit={
     if(sdfMap == null) init()
  }

}

object TreasureBoxTimeUtils{

  val treasureBoxTimeUtils:TreasureBoxTimeUtils = new TreasureBoxTimeUtils()

  def apply: TreasureBoxTimeUtils = new TreasureBoxTimeUtils()

  def msecBetweenStr(start:String,end:String):Long = {
    treasureBoxTimeUtils.msecBetweenStr(start,end)
  }
}
