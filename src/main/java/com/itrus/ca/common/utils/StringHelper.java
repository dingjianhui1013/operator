/**
 * 2009-10-12 21:47:02
 */
package com.itrus.ca.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;

/**
 * @author bin_liu
 */
public class StringHelper {

	private static String phoneMatcher;

	static {
		StringBuffer query = new StringBuffer();
		query.append("147\\d{9}");
		query.append("|1[3,8]\\d{9}");
		query.append("|15\\[0,1,2,3,5,6,7,8,9]");
		query.append("|17[3,6,7,8]\\d{8}");
		query.append("|170[0,1,2,5,7,8,9]\\d{7}");

		phoneMatcher = query.toString();
	}

	public static StringHelper getInstancle() {
		return new StringHelper();
	}

	private StringHelper() {

	}

	/*
	 * 得到当前年的最后一天
	 */

	public static Date getLastDateOfCurrentYear() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, currentYear);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	/*
	 * 得到指定日期和当前日期之间相差多少天
	 */
	public static Integer getDvalueDay(Date currentDate, Date expirationDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			currentDate = sdf.parse(sdf.format(currentDate));
			expirationDate = sdf.parse(sdf.format(expirationDate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(expirationDate);
			long time2 = cal.getTimeInMillis();

			return Integer.parseInt(String.valueOf((time2 - time1)
					/ (3600 * 24 * 1000)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/*
	 * 得到指定日期和当前日期之间相差多少年 小于365天按一年算 param expirationDate 制定日期
	 */
	public static Integer getDvalueYear(Date expirationDate) {

		Date currentDate = new Date();
		Integer days = getDvalueDay(currentDate, expirationDate);

		return (days / 365) + ((days % 365) > 0 ? 1 : 0);
	}

	/**
	 * 得到系统当前时间
	 * 
	 * @return String
	 */
	public static String getSystime() {
		DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dft.format(new Date());
	}

	/**
	 * 按格式获取系统当前时间
	 * 
	 * @param format
	 * @return String
	 */
	public static String getSystime(String format) {
		DateFormat dft = new SimpleDateFormat(format);
		return dft.format(new Date());
	}

	/**
	 * 按格式获取系统当前时间
	 * 
	 * @param format
	 * @return String
	 */
	public static String getSystime(String format, long timestamp) {
		DateFormat dft = new SimpleDateFormat(format);
		return dft.format(new Date(timestamp));
	}

	/**
	 * 得到上月的这一天0点，返回格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getPreMonth() {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

		curCal.add(Calendar.MONTH, -1);
		Date beginTime = curCal.getTime();
		String time = datef.format(beginTime) + " 00:00:00";
		return time;
	}

	/**
	 * 得到下月的这一天0点，返回格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getNextMonth() {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

		curCal.add(Calendar.MONTH, 1);
		Date beginTime = curCal.getTime();
		String time = datef.format(beginTime) + " 00:00:00";
		return time;
	}

	/**
	 * 指定时间的月份第一天
	 * 
	 * @param time
	 * @return String
	 */
	public static String getFirstDay(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = curCal.getTime();
		String sTime = datef.format(beginTime) + " 00:00:00";

		return sTime;
	}

	/**
	 * 指定时间的月份第一天
	 * 
	 * @param time
	 * @return String
	 */
	public static Long getFirstDayForTime(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = curCal.getTime();
		String sTime = datef.format(beginTime) + " 00:00:00";
		Long res = null;
		try {
			res = datef.parse(sTime).getTime();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 指定时间的月份第一天
	 * 
	 * @param time
	 * @return String
	 */
	public static Timestamp getFirstDayForTimestamp(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = curCal.getTime();
		String sTime = datef.format(beginTime) + " 00:00:00";
		Long res = null;
		try {
			res = datef.parse(sTime).getTime();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Timestamp(res);
	}

	/**
	 * 指定时间的月份最后一天
	 * 
	 * @param time
	 * @return String
	 */
	public static String getEndDay(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DATE, 1);
		curCal.roll(Calendar.DATE, -1);
		Date endTime = curCal.getTime();
		String eTime = datef.format(endTime) + " 23:59:59";

		return eTime;
	}

	/**
	 * 指定时间的月份最后一天
	 * 
	 * @param time
	 * @return Long
	 */
	public static Long getEndDayForTime(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DATE, 1);
		curCal.roll(Calendar.DATE, -1);
		Date endTime = curCal.getTime();
		String eTime = datef.format(endTime) + " 23:59:59";
		Long res = null;
		try {
			res = datef.parse(eTime).getTime();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 指定时间的月份最后一天
	 * 
	 * @param time
	 * @return Long
	 */
	public static Timestamp getEndDayForTimestamp(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DATE, 1);
		curCal.roll(Calendar.DATE, -1);
		Date endTime = curCal.getTime();
		String eTime = datef.format(endTime) + " 23:59:59";
		Long res = null;
		try {
			res = datef.parse(eTime).getTime();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Timestamp(res);
	}

	/**
	 * 得到默认日期格式的时间:yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 * @return Long
	 * @throws java.text.ParseException
	 */
	public static Long getTime(String time) throws java.text.ParseException {
		return getTime(time, "yyyy-MM-dd HH:mm:ss");
	}

	public static Timestamp getTimestamp(String time, String formart)
			throws java.text.ParseException {
		return new Timestamp(getTime(time, formart));
	}

	public static Timestamp getTimestamp(String time)
			throws java.text.ParseException {
		return new Timestamp(getTime(time));
	}

	/**
	 * 得到指定日期格式的时间
	 * 
	 * @param time
	 * @param formart
	 * @return Long
	 * @throws java.text.ParseException
	 */
	public static Long getTime(String time, String formart)
			throws java.text.ParseException {
		SimpleDateFormat datef = new SimpleDateFormat(formart);
		return datef.parse(time).getTime();
	}

	/**
	 * 当月第一天
	 * 
	 * @return String
	 */
	public static String getFirstDay() {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

		curCal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = curCal.getTime();
		String sTime = datef.format(beginTime) + " 00:00:00";

		return sTime;
	}

	/**
	 * 前月第一天
	 */
	public static Date getFirstDayOfLastMonth() {

		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		cal_1.add(Calendar.MONTH, -1);
		cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		return cal_1.getTime();
	}

	/**
	 * 当月最后一天
	 * 
	 * @return String
	 */
	public static String getEndDay() {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

		curCal.set(Calendar.DATE, 1);
		curCal.roll(Calendar.DATE, -1);
		Date endTime = curCal.getTime();
		String eTime = datef.format(endTime) + " 23:59:59";

		return eTime;
	}

	/**
	 * 前月最后一天
	 */
	public static Date getLastDayOfLastMonth() {

		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天
		return cale.getTime();

	}

	/**
	 * 得到指定类型时间间隔的之前日期
	 * 
	 * @param timeType
	 *            1-天，2-月<br>
	 * @param intervalNumber
	 *            间隔几天/几个月<br>
	 * @param currentTime
	 *            指定时间<br>
	 * @return Long
	 * @throws java.text.ParseException
	 */
	public static Long getBeforeTimeLong(int timeType, int intervalNumber,
			long currentTime) throws java.text.ParseException {
		String time = getBeforeTimeStr(timeType, intervalNumber, currentTime);
		return getTime(time);
	}

	/**
	 * 得到指定类型时间间隔的之前日期
	 * 
	 * @param timeType
	 *            1-天，2-月<br>
	 * @param intervalNumber
	 *            间隔几天/几个月<br>
	 * @param currentTime
	 *            指定时间<br>
	 * @return String
	 */
	public static String getBeforeTimeStr(int timeType, int intervalNumber,
			long currentTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(currentTime));

		if (timeType == 1)
			calendar.add(Calendar.DATE, -intervalNumber); // 得到前一天
		else if (timeType == 2)
			calendar.add(Calendar.MONTH, -intervalNumber); // 得到前一个月

		StringBuffer sb = new StringBuffer();
		sb.append(calendar.get(Calendar.YEAR)).append("-");
		sb.append(calendar.get(Calendar.MONTH) + 1).append("-");
		sb.append(calendar.get(Calendar.DATE)).append(" ");
		sb.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
		sb.append(calendar.get(Calendar.MINUTE)).append(":");
		sb.append(calendar.get(Calendar.SECOND));

		return sb.toString();
	}

	/**
	 * 得到UNICODE码
	 * 
	 * @param str
	 * @return String
	 */
	public static String getUnicode(String str) {

		if (str == null)
			return "";
		String hs = "";

		try {
			byte b[] = str.getBytes("UTF-16");
			for (int n = 0; n < b.length; n++) {
				str = (java.lang.Integer.toHexString(b[n] & 0XFF));
				if (str.length() == 1)
					hs = hs + "0" + str;
				else
					hs = hs + str;
				if (n < b.length - 1)
					hs = hs + "";
			}
			str = hs.toUpperCase().substring(4);
			char[] chs = str.toCharArray();
			str = "";
			for (int i = 0; i < chs.length; i = i + 4) {
				str += "\\u" + chs[i] + chs[i + 1] + chs[i + 2] + chs[i + 3];
			}
			return str;
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return str;
	}

	public int compare(Object o1, Object o2) {
		String a = (String) o1;
		String b = (String) o2;

		if (!isDigit(a) || !isDigit(b))
			throw new IllegalArgumentException("the object must a digit");

		long aa = Long.valueOf(a).longValue();
		long bb = Long.valueOf(b).longValue();

		if (aa > bb)
			return 1;
		else if (aa < bb)
			return -1;

		return 0;
	}

	/**
	 * 去空格并将其替换成指定字符
	 * 
	 * @param content
	 * @return String
	 */
	public static String alterSpace(String content, String character) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < content.length(); i++) {
			String c = new String(new char[] { content.charAt(i) });
			if (c.trim().length() == 0) {
				sb.append(character);
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * @param line
	 * @return boolean
	 */
	public static boolean isEmail(String line, int length) {
		return line.matches("\\w+[\\w.]*@[\\w.]+\\.\\w+$")
				&& line.length() <= length;
	}

	/**
	 * 判断输入是否全是中文
	 * 
	 * @param value
	 * @param length
	 * @return boolean
	 */
	public static boolean isChineseName(String value, int length) {
		return value.matches("^[\u4e00-\u9fa5]+$") && value.length() <= length;
	}

	/**
	 * 判断字符串是否含有HTML标签
	 * 
	 * @param value
	 * @return boolean
	 */

	public static boolean isHaveHtmlTag(String value) {
		return value.matches("<(\\S*?)[^>]*>.*?</\\1>|<.*? />");
	}

	/**
	 * 检查URL是否合法
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isURL(String value) {
		return value.matches("[a-zA-z]+://[^\\s]*");
	}

	/**
	 * 检查IP是否合法
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean iskIP(String value) {
		return value.matches("\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}");
	}

	/**
	 * 检查QQ是否合法，必须是数字，且首位不能字幕
	 * 
	 * @param value
	 * @return boolean
	 */

	public static boolean isQQ(String value) {
		return value.matches("[1-9][0-9]{4,13}");
	}

	/**
	 * 检查邮编是否合法
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isPostCode(String value) {
		return value.matches("[1-9]\\d{5}(?!\\d)");
	}

	/**
	 * 判定是否为有效的身份证
	 * 
	 * @param idcard
	 * @return boolean
	 */
	public static boolean isIdcard(String idcard) {
		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");// 匹配移动手机号码
		m = p.matcher(idcard);
		if (m.matches())
			return true;
		return false;
	}

	/**
	 * 检查输入的字符串是否为手机号
	 * 
	 * @param line
	 * @return List
	 */
	public static boolean isPhone(String line) {

		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(phoneMatcher);// 匹配移动手机号码
		m = p.matcher(line);
		if (m.matches())
			return true;
		return false;
	}

	/**
	 * 去掉手机号码前面的86和+86符号
	 * 
	 * @param phoneno
	 * @return String
	 */
	public static String fixPhoneno(String phoneno) {
		if (phoneno.length() > 11 && phoneno.startsWith("86"))
			return phoneno.substring(2);
		else if (phoneno.length() > 11 && phoneno.startsWith("+86"))
			return phoneno.substring(3);
		return phoneno;
	}

	/**
	 * 是否包含手机号
	 * 
	 * @param line
	 * @return boolean
	 */
	public static boolean hasPhone(String line) {
		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(phoneMatcher);// 匹配移动手机号码
		m = p.matcher(line);
		if (m.find())
			return true;
		return false;
	}

	/**
	 * 从一行字符串中提取号码由左至右,11为数字符合手机号规则;
	 * 
	 * @param line
	 * @return String
	 */
	public static String getPhone(String line) {
		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(phoneMatcher);// 匹配移动手机号码

		for (int i = 0; i < line.length(); i++) {

			m = p.matcher(line);
			if (m.find()) {
				String str = line.substring(m.start(), m.end());
				return str;
			}
		}

		return "";
	}

	/**
	 * 按规定条件得到一个文本里的字符
	 * 
	 * @param text
	 * @param compile
	 * @return Set
	 */
	public static Set getTextBlock(String text, String compile) {

		Set set = new HashSet();
		Pattern p = null; // 正则表达式
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(compile);// 匹配条件
		m = p.matcher(text);
		while (m.find()) {
			// System.out.println(strMail.substring(m.start(),m.end()));
			String str = text.substring(m.start(), m.end());
			set.add(str);
		}
		return set;
	}

	/**
	 * 返回所有手机号码
	 * 
	 * @param strMail
	 * @return Set
	 */
	public static Set getCode(String strMail) {
		/*
		 * int startpos = 0; for (int i = 0; i < 9; i++) { startpos =
		 * strMail.indexOf("\n", startpos) + 1; } if(startpos>=strMail.length())
		 * return; do { String str = strMail.substring(startpos + 7, startpos +
		 * 18); phonelog.info(str); //maillog.info(str); set.add(str); } while
		 * ((startpos = strMail.indexOf("\n", startpos) + 1) != -1 && startpos <
		 * strMail.length());
		 */
		Set set = new HashSet();
		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern.compile("1[3,5][4,5,6,7,8,9]\\d{8}|15[8,9]\\d{8}");// 匹配移动手机号码
		m = p.matcher(strMail);
		while (m.find()) {
			// System.out.println(strMail.substring(m.start(),m.end()));
			String str = strMail.substring(m.start(), m.end());
			set.add(str);
		}
		return set;
	}

	/**
	 * get email
	 * 
	 * @param content
	 * @return Set
	 */
	public static Set getMail(String content) {
		Set set = new HashSet();
		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern
				.compile("(?i)(?<=\\b)[a-z0-9][-a-z0-9_.]+[a-z0-9]@([a-z0-9][-a-z0-9]+\\.)+[a-z]{2,4}(?=\\b)");// 匹配移动手机号码
		m = p.matcher(content);
		while (m.find()) {
			// System.out.println(strMail.substring(m.start(),m.end()));
			String str = content.substring(m.start(), m.end());
			set.add(str);
		}
		return set;
	}

	/**
	 * 生成指定位之间的随机数
	 * 
	 * @param min
	 * @param max
	 * @return int
	 */
	public static int getRandom(int min, int max) {
		return (int) ((double) min + (int) (max - min) * Math.random());
	}

	/**
	 * 生成length位数字
	 * 
	 * @param length
	 * @return int
	 */
	public static int getRandom(int length) {
		return Integer.valueOf(getRand(length)).intValue();
	}// end...

	/**
	 * 生成length位数字
	 * 
	 * @param length
	 * @return long
	 */
	public static long getRandomL(int length) {
		return Long.valueOf(getRand(length)).longValue();
	}// end...

	/**
	 * 生成length位数字
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomStr(int length) {
		return Long.toString(getRandom(length));
	}// end...

	/**
	 * 生成随机数字的字符串
	 * 
	 * @param length
	 * @return String
	 */
	private static String getRand(int length) {
		StringBuffer t = new StringBuffer();
		for (int j = 0; j < length; j++) {
			double d = Math.random() * 10;
			int c = (int) d;
			t.append(c);
		}
		String result = t.toString();
		if (result.substring(0, 1).equalsIgnoreCase("0")) {
			result = result.replaceAll("0", "1");
		}

		if (result.length() > length) {
			result = result.substring(0, length);
		} else if (result.length() < length) {
			result = result + StringHelper.getRand(length - result.length());
		}

		return result;
	}// end...

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNull(String[] str) {
		for (int i = 0; i < str.length; i++) {
			if (isNull(str[i]))
				return true;
		}
		return false;
	}

	/**
	 * 字符串是否为数字
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNull(String str) {
		return (str == null || str.trim().length() == 0);
	}// end....

	/**
	 * 判断字符串中的每个字符是否都是数字
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isDigit(String[] str) {
		for (int i = 0; i < str.length; i++) {
			if (!isDigit(str[i]))
				return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否都是数字
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isDigit(String str) {
		if (isNull(str))
			throw new NullPointerException();
		for (int i = 0, size = str.length(); i < size; i++) {
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}
		return true;
	}// end....

	/**
	 * 得到STACK中信息
	 * 
	 * @param e
	 * @return String
	 */
	public static String getStackInfo(Throwable e) {
		StringBuffer info = new StringBuffer("Found Exception: ");

		info.append("\n");
		info.append(e.getClass().getName());
		info.append(" : ").append(e.getMessage() == null ? "" : e.getMessage());
		StackTraceElement[] st = e.getStackTrace();
		for (int i = 0; i < st.length; i++) {
			info.append("\t\n").append("at ");
			info.append(st[i].toString());
		}
		return info.toString();
	}// end..

	/**
	 * 将输入的字符按指定的正则式转换
	 * 
	 * @param str
	 * @param regEx
	 * @param code
	 * @return String
	 */
	private static String insteadCode(String str, String regEx, String code) {
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		String s = m.replaceAll(code);
		return s;
	}// end insteadCode method

	/**
	 * 将HTML的关键字替换输出
	 * 
	 * @param sourceStr
	 * @return String
	 */
	public static String toHtml(String sourceStr) {
		String targetStr;
		targetStr = insteadCode(sourceStr, ">", "&gt;");
		targetStr = insteadCode(targetStr, "<", "&lt;");
		targetStr = insteadCode(targetStr, "\n", "<br>");
		targetStr = insteadCode(targetStr, " ", "&nbsp;");
		return targetStr.trim();
	}// end toHTML method

	/**
	 * 转义传参中的特殊字符问题 <li>+ URL ??号表示空??%2B<br/> <li>空格 URL中的空格可以??号或者编??%20<br/>
	 * <li>/ 分隔目录和子目录 %2F <br/> <li>? 分隔实际??URL 和参??%3F<br/> <li>% 指定特殊字符 %25
	 * <br/> <li># 表示书签 %23 <br/> <li>& URL 中指定的参数间的分隔??%26 <br/> <li>=URL
	 * 中指定参数的??%3D <br/>
	 * 
	 * @param parameter
	 * @return String
	 */
	public static String sendGetParameter(String parameter) {
		parameter = insteadCode(parameter, "&", "%26");
		parameter = insteadCode(parameter, " ", "%20");
		parameter = insteadCode(parameter, "%", "%25");
		parameter = insteadCode(parameter, "#", "%23");

		return parameter.trim();
	}

	/**
	 * 按指定的起始和终止字符，切割字符??
	 * 
	 * @param content
	 * @param start
	 * @param end
	 * @return String
	 */
	public static String spiltStr(String content, String start, String end) {
		if (!(content.indexOf(start) > -1) || !(content.indexOf(end) > -1))
			throw new IndexOutOfBoundsException(
					"[start Character or end Character,isn't exist in the specified content]");

		int s = content.indexOf(start);

		int e = start.equals(end) ? content.substring(s + 1).indexOf(end)
				: content.indexOf(end);

		if (s >= e)
			throw new IndexOutOfBoundsException(
					"[the Character end is smallness Character start]");
		else
			content = new String(content.substring(s + 1, e));

		return content.trim();
	}// end...

	/**
	 * 得到????中按指定分割符切好的????元素
	 * 
	 * @param content
	 * @param split
	 * @return String[]
	 */
	public static String[] splitStr(String content, String split) {

		if (content.indexOf(split) < 0) {
			return new String[] { content };
		}

		int s = 0;
		int e = content.indexOf(split);

		List list = new ArrayList();

		while (e <= content.length()) {
			if (content.indexOf(split) == -1 && list.size() != 0) {
				list.add(content);
				break;
			}
			list.add(content.substring(s, e));
			content = content.substring(e + 1, content.length());
			e = s + content.indexOf(split);
		}
		return (String[]) list.toArray(new String[0]);
	}// end...

	/**
	 * 按指定的字符位切割字符串(用于页面显示),剩余位字符用变量end中的字符表示 此方法带过滤转意字符功能
	 * 
	 * @param str
	 * @param num
	 * @param end
	 * @return String
	 * @throws Cm2Exception
	 */
	public static String splitStr(String str, int num, String end) {
		StringBuffer sb = new StringBuffer();
		if (str == null || end == null)
			throw new NullPointerException();
		if (str.length() > num)
			str = sb.append(str.substring(0, num)).append(end).toString();
		return toHtml(str);
	}// end of splitStr()

	/**
	 * 按左补零右对齐的规则格式化内??
	 * 
	 * @param content
	 * @param count
	 * @return String
	 */
	public static String completeText(String content, int count) {
		StringBuffer sb = new StringBuffer();
		if (count > content.length()) {
			for (int i = count - content.length(); content.length() < count
					&& i != 0; i--)
				sb.append("0");
		}
		sb.append(content);
		return sb.toString();
	}// end..

	/**
	 * 按左补零右对齐的规则格式化内??
	 * 
	 * @param content
	 * @param count
	 * @return String
	 */
	public static String completeText(int content, int count) {
		String c = Integer.toString(content);
		StringBuffer sb = new StringBuffer();
		if (count > c.length()) {
			for (int i = count - c.length(); c.length() < count && i != 0; i--)
				sb.append("0");
		}
		sb.append(content);
		return sb.toString();
	}// end..

	/**
	 * 按右补空格左对齐的规则格式化内容
	 * 
	 * @param content
	 * @param count
	 * @return String
	 */
	public static String completeTextSpace(String content, int count) {
		StringBuffer sb = new StringBuffer();

		sb.append(content);
		if (count > content.length()) {
			for (int i = 0; i < count - content.length(); i++)
				sb.append(" ");
		}

		return sb.toString();
	}// end..

	/**
	 * 按右补空格左对齐的规则格式化内容
	 * 
	 * @param content
	 * @param count
	 * @return String
	 */
	public static String completeTextSpace(int content, int count) {
		StringBuffer sb = new StringBuffer();
		String c = Integer.toString(content);
		sb.append(content);
		if (count > c.length()) {
			for (int i = 0; i < count - c.length(); i++)
				sb.append(" ");
		}
		return sb.toString();
	}// end..

	/**
	 * 特殊处理号码
	 * 
	 * @param phone
	 * @return String
	 */
	public static String processPhone(String phone) {
		StringBuffer sb = new StringBuffer();
		sb.append(phone.substring(0, 3)); // 3
		sb.append(phone.substring(5, 6)); // 6
		sb.append(phone.substring(4, 5)); // 5
		sb.append(phone.substring(3, 4)); // 4
		sb.append(phone.substring(6, 7)); // 7
		sb.append(phone.substring(9, 10)); // 10
		sb.append(phone.substring(7, 8)); // 8
		sb.append(phone.substring(8, 9)); // 9
		sb.append(phone.substring(10, 11)); // 11

		return sb.toString();
	}

	public static String toHex(String phoneno) {
		long i = new Long(phoneno).longValue();
		String i_16 = Long.toHexString(i);
		return i_16;
	}

	/**
	 * 判断字符是否是中文
	 * 
	 * @param c
	 * @return boolean
	 */
	public static boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

		|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

		|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

		|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

		|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

		|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		}

		return false;

	}

	/**
	 * @param b
	 * @return String
	 */
	public static String getBASE64(byte[] b) {
		String s = null;
		if (b != null) {
			s = new sun.misc.BASE64Encoder().encode(b);
		}
		return s;
	}

	/**
	 * @param s
	 * @return byte[]
	 */
	public static byte[] getFromBASE64(String s) {
		byte[] b = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				return b;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	/**
	 * 把链接的中文转成可识别链
	 * 
	 * @param content
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String getUrlEncode(String content)
			throws UnsupportedEncodingException {
		char[] a = content.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			if (isChinese(a[i])) {
				sb.append(URLEncoder.encode(a[i] + "", "GBK"));
			} else {
				sb.append(a[i]);
			}
		}

		return sb.toString();
	}

	public static String getUrlEncode(String content, String encode)
			throws UnsupportedEncodingException {
		char[] a = content.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			if (isChinese(a[i])) {
				sb.append(URLEncoder.encode(a[i] + "", encode));
			} else {
				sb.append(a[i]);
			}
		}

		return sb.toString();
	}

	public static String getUrlDecode(String content)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append(URLDecoder.decode(content, "GBK"));
		return sb.toString();
	}

	public static String getUrlDecode(String content, String encode)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append(URLDecoder.decode(content, encode));
		return sb.toString();
	}

	/**
	 * 转换十六进制编码为字符串
	 * 
	 * @param bytes
	 * @return String
	 */
	public static String toStringHex(String bytes) {

		String hexString = "0123456789ABCDEF ";
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	/**
	 * 转换为16进制字符串
	 * 
	 * @param str
	 * @return String
	 */
	public static String toHexString(String str) {
		String hexString = "0123456789ABCDEF ";
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();

	}

	/**
	 * 取一个整数的指定百分比的整数
	 * 
	 * @param max
	 * @param perc
	 * @return Integer
	 */
	public static Integer findPerc(Integer max, Integer perc) {
		Double a = max * (perc * 0.01);
		return new Long(Math.round(a)).intValue();
	}

	/**
	 * 对Map , key=String的进行key排序
	 * 
	 * @param m
	 * @param isDesc
	 * @return Map<String, Object>
	 */
	public static Map sortMap(Map m, final boolean isDesc) {
		List list_Data = new ArrayList(m.keySet());

		Collections.sort(list_Data, new Comparator<String>() {
			public int compare(String o1, String o2) {
				int res = 0;
				if (!isDesc)
					res = o1.compareTo(o2);
				else
					res = o2.compareTo(o1);
				return res;
			}
		});

		Map m2 = new LinkedHashMap();

		for (Object e : list_Data) {
			m2.put(e, m.get(e));
		}
		return m2;
	}

	public static Double formartDecimalToDouble(Double d) {
		return new Double(formartDecimalToStr(d));
	}

	public static String formartDecimalToStr(Double d) {
		return formartDecimalToStr(d, "0.00");
	}

	public static String formartDecimalToStr(Float d) {
		return formartDecimalToStr(d.doubleValue(), "0.00");
	}

	public static Double formartDecimalToDouble(Double d, String formart) {
		return new Double(formartDecimalToStr(d, formart));
	}

	public static String formartDecimalToStr(Double d, String formart) {
		DecimalFormat decimalFormat = new DecimalFormat(formart);
		String resultStr = decimalFormat.format(d);
		return resultStr;
	}

	public static void main(String[] args) throws Exception {

		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		Date s = datef.parse("2012-10-15");
		Date e = datef.parse("2012-12-15");
		// datef.parse("2012-10-15");
		System.out.println("------------------------");
		System.out.println(getFirstDay(s.getTime()));
		System.out.println(getEndDay(e.getTime()));
		System.out.println("------------------------");
		System.out.println(getBeforeTimeStr(2, 3, System.currentTimeMillis()));
		System.out.println(getSystime("yyyy_MM-dd",
				getBeforeTimeLong(2, 3, System.currentTimeMillis())));
	}
}
