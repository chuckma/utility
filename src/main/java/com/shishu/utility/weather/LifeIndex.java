package com.shishu.utility.weather;

import java.util.HashMap;

public class LifeIndex {
	private static HashMap<String, String> coldIndex = new HashMap<String, String>(); // 感冒指数
	private static HashMap<String, String> ultIndex = new HashMap<String, String>(); // 紫外线指数
	private static HashMap<String, String> clotheIndex = new HashMap<String, String>(); // 穿衣指数
	private static HashMap<String, String> travelIndex = new HashMap<String, String>(); // 旅行指数
	private static HashMap<String, String> exerciseIndex = new HashMap<String, String>(); // 运动指数
	private static HashMap<String, String> comfortIndex = new HashMap<String, String>(); // 舒适指数
	private static HashMap<String, String> allergyIndex = new HashMap<String, String>(); // 过敏指数
	private static HashMap<String, String> dressIndex = new HashMap<String, String>(); // 化妆指数
	private static HashMap<String, String> washCarIndex = new HashMap<String, String>(); // 洗车指数

	public static String getColdSug(String level) {
		return "建议着棉衣加羊毛衫等冬季服装。";
	}

	public static String getUltSug(String level) {
		return "辐射弱，涂擦SPF8-12防晒护肤品。";
	}

	public static String getClotheSug(String level) {
		return "建议着棉衣加羊毛衫等冬季服装。";
	}

	public static String getTravelSug(String level) {
		return "风稍大，体感稍凉，外出注意保暖。";
	}

	public static String getExerciseSug(String level) {
		return "推荐您在室内进行低强度运动。";
	}

	public static String getComfortSug(String level) {
		return "白天阴沉，有点凉。";
	}

	public static String getAllergySug(String level) {
		return "无需担心过敏，可放心外出，享受生活。";
	}

	public static String getDressSug(String level) {
		return "请选用保湿型霜类化妆品。";
	}

	public static String getWashCarSug(String level) {
		return "天气较好，适合擦洗汽车。";
	}

	public static String getSug(String type, String level) {
		if (type.equals("过敏指数")) {
			return getAllergySug(level);
		} else if (type.equals("感冒指数")) {
			return getColdSug(level);
		} else if (type.equals("舒适指数")) {
			return getComfortSug(level);
		} else if (type.equals("化妆指数")) {
			return getDressSug(level);
		} else if (type.equals("运动指数")) {
			return getExerciseSug(level);
		} else if (type.equals("旅游指数")) {
			return getTravelSug(level);
		} else if (type.equals("紫外线指数")) {
			return getUltSug(level);
		} else if (type.equals("洗车指数")) {
			return getWashCarSug(level);
		} else 
			return getColdSug(level);
	}
}
