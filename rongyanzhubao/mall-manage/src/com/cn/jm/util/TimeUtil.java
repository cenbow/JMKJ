package com.cn.jm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	
	/**
	 * 
	* 方法说明：分钟数转换*天*小时*分钟
	* 创建时间：2017年5月16日下午3:27:18
	* By 李智胜
	* @param mins
	* @return
	**
	 */
	public static String formatMin(int mins){
		int min = mins%60;
		int hours = mins/60;
		int day = hours/24;
		int hour = hours%24;
		return day+"天"+hour+"小时"+min+"分钟";
	}
	
	/**
	 * 
	* 方法说明：分钟数转小时和分钟
	* 创建时间：2017年5月16日下午3:34:59
	* By 李智胜
	* @param mins
	* @return
	**
	 */
	public static String formatHourMin(int mins){
		int min = mins%60;
		int hours = mins/60;
		return hours+"小时"+min+"分钟";
	}
	
	/**
	 * 
	* 方法说明：计算两个时间戳的天数差
	* 创建时间：2017年5月17日下午3:35:00
	* By 李智胜
	* @param start
	* @param end
	* @return
	**
	 */
	public static int subDate(Date start,Date end){
		long ms = end.getTime() - start.getTime();
		long s = ms/1000;
		int hours = (int) (s/3600);
		int days = hours/24;
		return days;
	}
	
	/**
	 * 日期格式转换
	 */
	public static String format(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date;
		try {
			date = sdf.parse(time);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf2.format(date);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 
	* 方法说明：格式化时间为yyddmm
	* 创建时间：2017年7月18日下午2:47:41
	* By 李智胜
	* @param date
	* @return
	**
	 */
	public static String formatStr(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String time = sdf.format(date);
		return time ;
	}

	/**
	 * 
	 * @param date
	 * @param timeFormat 传入时间字符串的格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatStr(Date date,String timeFormat){
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		return sdf.format(date) ;
	}
	
	public static String formatStrDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		return time ;
	}
	
	public static String now(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date=new Date();  
		return sdf.format(date);  
	}
	
	/**
	 * 
	* 方法说明：获取当前日期转换String
	* 创建时间：2017年9月1日下午6:37:18
	* By 李智胜
	* @return
	**
	 */
	public static String nowWithoutTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		java.util.Date date=new java.util.Date();  
		return sdf.format(date);  
	}
	
	/**
	 * 
	* 方法说明：输入日期增加(正数)/减少(负数)若干天（days）,返回日期格式
	* 创建时间：2017年7月13日上午10:49:28
	* By 李智胜
	* @param days
	* @return
	**
	 */
	public static Date addReduceDaysD(int days,Date date){
       Calendar c = Calendar.getInstance();
       c.setTime(date);
       c.add(Calendar.DAY_OF_MONTH, days);
       return c.getTime();
	}
	
	/**
	 * 
	* 方法说明：输入日期增加(正数)/减少(负数)若干天（days），返回String格式
	* 创建时间：2017年7月13日上午10:55:05
	* By 李智胜
	* @param days
	* @return
	**
	 */
	public static String addReduceDaysS(int days,Date date){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.DAY_OF_MONTH, days);
	    return sf.format(c.getTime());
	}
	
	public static Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		Date date;
		try {
			date = formatter.parse(dateString);
			return date;
		} catch (ParseException e) {
			return null;
		}
		
	}

	/**
	* 方法说明：将字符串转换为date对象
	* @param date 字符串的时间 2018-01-01 11:11:11
	* @param timeFormat 传入时间字符串的格式 yyyy-MM-dd HH:mm:ss
	* @return
	 * @throws ParseException 
	*/
	public static Date parse(String date,String timeFormat) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		return sdf.parse(date);
	}
	
	public static void main(String[] args) throws ParseException {
		Date time1 = new Date();
		Date time2 = parse("2019-02-26","yyyy-MM-dd");
		String formatStr1 = formatStr(time1,"MM-dd");
		String formatStr2 = formatStr(time2,"MM-dd");
		System.out.println(formatStr1+"-"+formatStr2);
		System.out.println(formatStr1.equals(formatStr2));
	}

	/**
	 * 根据开始时间和结束时间设置时间段样式
	 * 将样式为 startTimeFormat-endTimeFormat 样子输出
	 * @param startTime
	 * @param endTime
	 * @param startTimeFormat 传入时间字符串的格式 yyyy-MM-dd HH:mm:ss
	 * @param endTimeFormat 传入时间字符串的格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getTimeStyle(Date startTime, Date endTime,String startTimeFormat,String endTimeFormat) {
		if(startTime == null || endTime == null) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat(startTimeFormat);
		SimpleDateFormat ef = null;
		//如果转换的时间都一致,就不用再new出一个新的对象
		if(startTimeFormat.equals(endTimeFormat)) {
			ef = sf;
		}else {
			ef = new SimpleDateFormat(endTimeFormat);
		}
		return sf.format(startTime) + " - " + ef.format(endTime);
	}

	/**
	 * 判断开始时间和结束时间是否同一天
	 * 将样式为 startTimeFormat-endTimeFormat 样子输出
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isSameDay(Date startTime, Date endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(startTime).equals(sdf.format(endTime));
	}
	
}
