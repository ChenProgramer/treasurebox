package com.cgr.utils.timeutils

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import java.util.regex.Matcher
import java.util.regex.Pattern

import com.cgr.consts.TimeReferenseConsts

/**
  * Created by guorui.chen on 2017/3/29.
  */
object TreasureBoxTimeUtils{

  val treasureBoxTimeUtils:TreasureBoxTimeUtils = new TreasureBoxTimeUtils()

  /**
    * 注册自定义的 格式 和 正则表达式
    * switch : 是否保留当前工具内的 格式
    */
  def registerFormat(format:String,pattern:String,switch:Boolean):Unit={
    treasureBoxTimeUtils.registerFormat(format,pattern,switch)
  }

  /**
    * 两个日期字符串之间的毫秒值
    */
  def msecBetweenStr(start:String,end:String):Long = {
    treasureBoxTimeUtils.msecBetweenStr(start,end)
  }

  /**
    * 移动天数
    */
  def moveDay(originDay:String,days:Integer):String={
    treasureBoxTimeUtils.moveDay(originDay,days)
  }

  /**
    * 移动月数
    */
  def moveMonth(originDay:String,days:Integer):String={
    treasureBoxTimeUtils.moveMonth(originDay,days)
  }

  /**
    * 移动年数
    */
  def moveYear(originDay:String,days:Integer):String={
    treasureBoxTimeUtils.moveYear(originDay,days)
  }
}

class TreasureBoxTimeUtils private(){

  var strList = TimeReferenseConsts.strList

  var patternMap:Map[String,String] = TimeReferenseConsts.patternMap

  var sdfMap:Map[String,SimpleDateFormat] = null

  val calender = Calendar.getInstance()

  private def init(): Unit ={
    sdfMap = strList.map(x => (x,new SimpleDateFormat(x))).toMap
  }

  def registerFormat(format:String,pattern:String,switch:Boolean):Unit={
    require(format,pattern)
    switch match{
      case true =>{
        strList = strList :+ format
        patternMap = patternMap + (format -> pattern)
      }
      case false =>{
        strList = List(format)
        patternMap = Map(format -> pattern)
      }
      case _ =>{
        strList = List(format)
        patternMap = Map(format -> pattern)
      }
    }
    init()
  }

  def msecBetweenStr(start:String,end:String):Long = {
    require(start,end)
    val msecOfStart = parseString(start).getTime
    val msecOfEnd = parseString(end).getTime
    msecOfEnd - msecOfStart
  }

  private def parseString(str:String):Date={
    val sdfs = strList.filter( x=> str.matches(patternMap(x)))
    if(sdfs.size == 0) throw new Exception("[TreasureBox] [TreasureBoxTimeUtils] : There it is a illlegal data param When parse String:"+str)
    sdfMap(sdfs.apply(0)).parse(str)
  }

  def moveDay(originDay:String,days:Integer):String={
    require(originDay,days)
    val date = parseString(originDay)
    calender.setTime(date)
    calender.add(Calendar.DAY_OF_YEAR,days)
    sdfMap(strList.filter( x=> originDay.matches(patternMap(x))).apply(0)).format(calender.getTime)
  }

  def moveMonth(originDay:String,months:Integer):String={
    require(originDay,months)
    val date = parseString(originDay)
    calender.setTime(date)
    calender.add(Calendar.MONTH,months)
    sdfMap(strList.filter( x=> originDay.matches(patternMap(x))).apply(0)).format(calender.getTime)
  }

  def moveYear(originDay:String,years:Integer):String={
    require(originDay,years)
    val date = parseString(originDay)
    calender.setTime(date)
    calender.add(Calendar.YEAR,years)
    sdfMap(strList.filter( x=> originDay.matches(patternMap(x))).apply(0)).format(calender.getTime)
  }

  private def require(params:Any*):Unit={
    params.foreach{
      case(any) =>
        any match{
          case null => throw new Exception("[TreasureBox] [TreasureBoxTimeUtils] : There it is a null param When require")
          case "" => throw new Exception("[TreasureBox] [TreasureBoxTimeUtils] : There it is a empty param When require, param:"+any.toString)
          case _ => null
        }
    }
  }

  {
    init()
  }

}


