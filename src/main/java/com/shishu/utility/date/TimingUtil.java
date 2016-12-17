package com.shishu.utility.date;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wangtao 2013-12-12
 * 
 */
public class TimingUtil {

	private static long[] TIME_FACTOR = { 60 * 60 * 1000, 60 * 1000, 1000 };
	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd,HH");
	public static SimpleDateFormat sdf3 = new SimpleDateFormat("yy-MM-dd HH:mm");
	public static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat sdf5 = new SimpleDateFormat("dd日HH时");
	public static SimpleDateFormat sdf6 = new SimpleDateFormat("yyyyMM");
	public static SimpleDateFormat sdf7 = new SimpleDateFormat("MM月dd日");
	public static SimpleDateFormat sdf8 = new SimpleDateFormat("yyyyM");
	public static SimpleDateFormat sdf9 = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdf11 = new SimpleDateFormat("yyyy-MM-dd,HH");
	public static final Logger LOG = LoggerFactory.getLogger(TimingUtil.class);
	private static long now = System.currentTimeMillis();
	/**
	 * Calculate the elapsed time between two times specified in milliseconds.
	 * 
	 * @param start
	 *            The start of the time period
	 * @param end
	 *            The end of the time period
	 * @return a string of the form "XhYmZs" when the elapsed time is X hours, Y
	 *         minutes and Z seconds or null if start > end.
	 */
	public static String elapsedTime(long start, long end) {
		if (start > end) {
			return null;
		}

		long[] elapsedTime = new long[TIME_FACTOR.length];

		for (int i = 0; i < TIME_FACTOR.length; i++) {
			elapsedTime[i] = start > end ? -1 : (end - start) / TIME_FACTOR[i];
			start += TIME_FACTOR[i] * elapsedTime[i];
		}

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < elapsedTime.length; i++) {
			if (i > 0) {
				buf.append(":");
			}
			buf.append(nf.format(elapsedTime[i]));
		}
		return buf.toString();
	}

	/**
	 * 格式化时间 格式是2013-12-12 10:28:15
	 */
	public static String getFormatTime(String oldTime) {
		oldTime = oldTime.trim();
		
		//add by wgh
		if(oldTime.indexOf(" ") < 0 && oldTime.indexOf(":", 10) > -1){
			StringBuilder stringBuilder = new StringBuilder(oldTime);
        	int ind = stringBuilder.indexOf(":", 10);
        	stringBuilder.insert(ind-2, " ");
        	oldTime = stringBuilder.toString();
        }

		String regular1 = "201\\d-(0\\d|1[0-2]{1})-[0-3]{1}\\d [0-2]{1}\\d:[0-6]{1}\\d:[0-6]{1}\\d";
		String regular2 = "201\\d-(0\\d|1[0-2]{1})-[0-3]{1}\\d";
		String regular3 = "[0-2]{1}\\d:[0-6]{1}\\d:[0-6]{1}\\d";
		String regular4 = "201\\d";
		String regular5 = "0\\d|1[0-2]{1}";
		String regular6 = "0\\d|1\\d|2[0-3]{1}";
		String regular7 = "0\\d|[1-2]{1}\\d|3[0-1]{1}";
		String regular8 = "0\\d|[1-5]{1}\\d";
		String regular9 = "\\d";

		String formatTime = "";

		if (!Pattern.matches(regular1, oldTime)) {
			String oldTimeArray[] = oldTime.split(" ");
			int oldTimeLen = oldTimeArray.length;

			String ymd = "";
			String hms = "";

			if (Pattern.matches(regular2, oldTimeArray[0])) {
				ymd = oldTimeArray[0].trim();
			} else {
				String ymdArray[] = oldTimeArray[0].split("-");
				switch (ymdArray.length) {
				case 1:
					if (Pattern.matches(regular4, ymdArray[0])) {
						formatTime = oldTimeArray[0].trim() + "-01-01 " + "00:00:00";
					} else {
						System.out.println("不符合格式：[" + oldTime + "]");
						return "";
					}
					break;
				case 2:
					if (Pattern.matches(regular4, ymdArray[0]) && Pattern.matches(regular5, ymdArray[1])) {
						ymd = oldTimeArray[0].trim() + "-01";
					} else {
						System.out.println("不符合格式：[" + oldTime + "]");
						return "";
					}
					break;
				case 3:
					if (Pattern.matches(regular4, ymdArray[0])
							&& (Pattern.matches(regular5, ymdArray[1]) || Pattern.matches(regular9, ymdArray[1])
									&& (Pattern.matches(regular7, ymdArray[2]) || Pattern.matches(regular9, ymdArray[2])))) {
						for (int i = 0; i < ymdArray.length; i++) {
							ymd += addZero(ymdArray[i]);
							if (i < ymdArray.length - 1) {
								ymd += "-";
							}
						}
					} else {
						System.out.println("不符合格式：[" + oldTime + "]");
						return "";
					}
					break;
				default:
					System.out.println("不符合格式：[" + oldTime + "]");
					return "";
				}
			}
			formatTime = ymd + " 00:00:00";

			if (oldTimeLen == 2) {
				String hmsArray[] = oldTimeArray[1].split(":");

				if (Pattern.matches(regular3, oldTimeArray[1])) {
					hms = oldTimeArray[1].trim();
				} else {
					String newHmsArray[] = "00:00:00".split(":");

					if (hmsArray.length > 1 && hmsArray.length < 4) {
						if ((Pattern.matches(regular6, hmsArray[0]) || Pattern.matches(regular9, hmsArray[0]))) {
							newHmsArray[0] = addZero(hmsArray[0]);
						} else {
							System.out.println("不符合格式：[" + oldTime + "]");
							return "";
						}

						for (int i = 1; i < hmsArray.length; i++) {
							if ((Pattern.matches(regular8, hmsArray[i]) || Pattern.matches(regular9, hmsArray[i]))) {
								newHmsArray[i] = addZero(hmsArray[i]);
							} else {
								System.out.println("不符合格式：[" + oldTime + "]");
								return "";
							}
						}
						hms = newHmsArray[0] + ":" + newHmsArray[1] + ":" + newHmsArray[2];
					} else {
						System.out.println("不符合格式：[" + oldTime + "]");
						return "";
					}
				}
				formatTime = ymd + " " + hms;
			} else if (oldTimeLen > 2) {
				System.out.println("不符合格式：[" + oldTime + "]");
				return "";
			}
		} else {
			formatTime = oldTime;
		}

		return formatTime;
	}

	public static String addZero(String num) {
		int len = num.length();

		if (len == 0) {
			num = "00";
		} else if (len == 1) {
			num = "0" + num;
		}
		return num;
	}

	/**
	 * 从字符串中提取时间
	 */
	public static String getTime(String text) {
		Pattern pattern = Pattern.compile("201[0-9]{1}[0-9-/: ：年月　日 ]{5,30}");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			Matcher matcher2 = Pattern.compile("[年月]").matcher(matcher.group(0).trim());
			Matcher matcher3 = Pattern.compile("日").matcher(matcher2.replaceAll("-"));
			Matcher matcher4 = Pattern.compile("/").matcher(matcher3.replaceAll(" "));
			text = matcher4.replaceAll("-");

			StringBuilder stringBuilder = new StringBuilder(text);

			Matcher matcher5 = Pattern.compile("[0-9-]{8,10}").matcher(text);

			if (matcher5.find()) {
				text = matcher5.group(0);
				if (text.split("-").length > 2) {

					if (text.split("-")[1].length() < 2) {
						stringBuilder.insert(5, '0');
					}

					if (text.split("-")[2].length() < 2) {
						stringBuilder.insert(8, '0');
					}
				}
			}
			String results[] = stringBuilder.toString().split("[\\s\\p{Zs}]");// 同时匹配 \s 以及各种其他的空白字符（包括全角空格等）
			return getFormatTime(results.length > 1 ? results[0] + " " + results[results.length - 1] : results[0]);
		}
		return "";
	}

	/**
	 * 获得当前年份
	 */
	public static int getThisYear() {
		String year = new SimpleDateFormat("yyyy").format(now);
		return Integer.parseInt(year);
	}
	
	/**
	 * 获得当前月份
	 */
	public static int getThisMonth() {
		String year = new SimpleDateFormat("MM").format(now);
		return Integer.parseInt(year);
	}
	
	/**
	 * 获得当前天
	 */
	public static int getThisDay() {
		String year = new SimpleDateFormat("dd").format(now);
		return Integer.parseInt(year);
	}
	
	/**
	 * 获得当前小时
	 */
	public static int getThisHour() {
		String year = new SimpleDateFormat("HH").format(now);
		return Integer.parseInt(year);
	}
	
	/**
	 * 获得当前分钟
	 */
	public static int getThisMinute() {
		String year = new SimpleDateFormat("mm").format(now);
		return Integer.parseInt(year);
	}
	
	/**
	 * 获得当前秒数
	 */
	public static int getThisSecond() {
		String year = new SimpleDateFormat("ss").format(now);
		return Integer.parseInt(year);
	}
	
	/**
	 * 获得当前某年某月
	 */
	public static String  getThisYearAndMonth() {
		return sdf6.format(now);
	}
	
	/**
	 * 获得当前某月某日
	 */
	public static String  getThisMonthAndDay() {
		return sdf7.format(now);
	}
	
	/**
	 * 获得当前某日某时
	 */
	public static String  getThisDayAndHour() {
		return sdf5.format(now);
	}
	
	@SuppressWarnings("deprecation")
	public static String getNowString() {
		return getNow().toLocaleString();
	}
	
	/**
	 * 获得当前时间
	 */
	public static Date getNow() {
		Date date = new Date(now);
		return date;
	}
	
	public static String getTextExceptTime(String text) {
		return text.replaceAll("20[0-9]{2}[0-9-/: ：年月　日 ]{5,30}", "");
	}

	public static void main(String[] args) {
		String imeString1 = "2013-09-023:00:00";
		System.out.println(getFormatTime(imeString1));
	}
}
