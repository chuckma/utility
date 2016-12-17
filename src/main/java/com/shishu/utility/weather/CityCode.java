package com.shishu.utility.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CityCode {
	private static HashMap<String,ArrayList<Integer>> city_region = new HashMap<String,ArrayList<Integer>>();
	private static HashMap<Integer,String> region_code = new HashMap<Integer,String>();
	static {
		try {
			BufferedReader read = new BufferedReader(new InputStreamReader(CityCode.class.getClassLoader().getResourceAsStream("weather_mapping.txt")));
			String line = null;
			while((line=read.readLine())!=null) {
				String[] split = line.split(",");
				if(split.length==3) {
					Integer regioncode = Integer.valueOf(split[0]);
					String city = split[1];
					String region = split[2];
					if(city_region.containsKey(city)) {
						city_region.get(city).add(regioncode);
					}else {
						ArrayList<Integer> value = new ArrayList<Integer>();
						value.add(regioncode);
						city_region.put(city, value);
					}
					region_code.put(regioncode,region);
				}
			}
			read.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<Integer> getCityRegionsCode(String cityname) {
		return city_region.get(cityname);
	}
	
	public static ArrayList<String> getCityRegionsName(String cityname) {
		ArrayList<Integer> regionlist = getCityRegionsCode(cityname);
		ArrayList<String> result = new ArrayList<String>();
		for(Integer region:regionlist) {
			result.add(region_code.get(region));
		}
		return result;
	}
	
	public static HashMap<Integer, String> getCityRegionsMap(String cityname) {
		ArrayList<Integer> regionlist = getCityRegionsCode(cityname);
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		for(Integer region:regionlist) {
			result.put(region, region_code.get(region));
		}
		return result;
	}
	
	public static Integer getRegionCode(String city, String region) {
		ArrayList<Integer> regioncodelist = getCityRegionsCode(city);
		for(Integer code: regioncodelist) {
			if(region_code.get(code).equals(region))
				return code;
		}
		return null;
	}
	
	public static String getRegionName(Integer code) {
		return region_code.get(code);
	}
	
	public static void main(String[] args) {
		System.out.println(getCityRegionsMap("杭州"));
		//System.out.println(getCityRegion("杭州").size());
	}	
}
