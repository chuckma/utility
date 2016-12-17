package com.shishu.utility.weather;

import java.util.HashMap;
import java.util.Map;

public class WeatherLevelTool {

	private static Map<String, Double> map = new HashMap<String, Double>();
	static {
		map.put("暴雪", 10.0);
		map.put("大雪", 9.0);
		map.put("中雪", 8.0);
		map.put("阵雪", 7.0);
		map.put("小雪", 6.0);
		map.put("雨夹雪", 6.0);
		
		map.put("暴雨", 10.0);
		map.put("大雨", 9.0);
		map.put("中雨", 8.0);
		map.put("阵雨", 7.0);
		map.put("雷雨", 7.0);
		map.put("小雨", 6.0);
		map.put("冻雨", 6.0);
		
		map.put("晴", 5.0);
		map.put("阴", 4.0);
		map.put("多云", 3.0);
		map.put("雾", 2.0);
		
		map.put("霾", 1.0);
		map.put("沙尘暴", 1.0);
		map.put("扬沙", 1.0);
	}

	public static double getWeatherValue(String name) {
		double weatherValue = 0.0;
		int count = 0;
		for (String weather : map.keySet()) {
			if (name.contains(weather)) {
				weatherValue += map.get(weather);
				count++;
			}
		}
		if (count == 0) {
			System.out.println(name);
			return 0.0;
		}
		else {
			weatherValue = weatherValue / count;
			return weatherValue;
		}
	}

	public static String getWeatherName(double value) {
		for (String key : map.keySet()) {
			if (map.get(key) == value) {
				return key;
			}
		}
		return null;
	}
}
