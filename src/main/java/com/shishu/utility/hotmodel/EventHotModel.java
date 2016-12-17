package com.shishu.utility.hotmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.shishu.utility.log.LogUtil;
import com.shishu.utility.math.MathTool;

public class EventHotModel {
	/**
	 * @author wgh
	 * 事件热度模型
	 */
	private TreeMap<Long, Integer> opinionTimeLine;
	
	private static final float MIN_ALL_HOT = 15.0f;//事件总热度大于这个值认为是预警事件
	private static final float MIN_RATE = 1.0f;//事件平均上升率大于这个值认为是预警事件
	
	private float rate;
	private List<Float> rateLine;
	private int eventhot;
	private List<float[]> linePoints;
	
	private Logger log = LogUtil.getLogger(EventHotModel.class);
	
	public EventHotModel(TreeMap<Long, Integer> opinionTimeLine){
		this.opinionTimeLine = opinionTimeLine;
		linePoints = getLinePoints();
		computerEventHot();
		computerRate();
	}

	/**
	 * 计算事件总热度
	 * 
	 * @return
	 */
	public void computerEventHot() {
		int hotScore = 0;
		int size = opinionTimeLine.size();
		int i = 0;
		for (long key : opinionTimeLine.keySet()) {
			i++;
			hotScore += opinionTimeLine.get(key) * (1 + (float) i / size);
		}
		eventhot = hotScore;
	}

	/**
	 * 计算事件的上升速度
	 * 
	 * @return
	 */
	public void computerRate() {
		float all_rate = 0.0f;
		rateLine = new ArrayList<Float>();
		int l = linePoints.size();
		if (l < 2){
			rate = 0.0f;
			return;
		}
		for (int i = 1; i < l; i++) {
			float point_rate = (linePoints.get(i)[1] - linePoints.get(i - 1)[1])
					/ (linePoints.get(i)[0] - linePoints.get(i - 1)[0]);
			point_rate = point_rate * (1+(float)i/l);
			all_rate += point_rate;
			rateLine.add(point_rate);
		}
		rate = MathTool.getFloatCut(all_rate / (l - 1), 1);
	}
	
	/**
	 * 事件预警等级
	 * @return
	 */
	public String getLevel(){
		float score = eventhot * (rate * 5 + 1);
		if(score > 300)
			return "A";
		else if(score > 150)
			return "B";
		else
			return "C";
	}

	/**
	 * 判断事件是否为预警事件,判断依据有两个，一个是事件热度，一个是事件的发展趋势.满足以下条件一个则认为是预警事件：
	 * 1.事件总热度大于某个值（MIN_ALL_HOT） 
	 * 2.事件平均热度上升趋势大于某个值（MIN_RATE）
	 * 3.事件有3次以上的时间有报道，发展总趋势为上升，且最后3个时间点的上升率逐次增加(都大于0)
	 * 
	 * @return
	 */
	public boolean isWarningOpinion() {
		if (getEventhot() > MIN_ALL_HOT) {
			return true;
		} else {
			log.debug("computer if warning by rate");
			if (rate > 0) {
				if (rate > MIN_RATE)
					return true;
				else if (rateLine.size() >= 2
						&& rateLine.get(rateLine.size() - 1) > rateLine.get(rateLine.size() - 2)
						&& rateLine.get(rateLine.size() - 2) > 0)
					return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 获取事件的发展时间轴，以x-y坐标来表示，x代表时间，y代表该时间下的與情热度
	 * @return
	 */
	public List<float[]> getLinePoints() {
		List<float[]> linePoints = new ArrayList<float[]>();
		long startkey = 0l;
		float allscore = 0;
		float[] lastpoint = new float[2];
		lastpoint[0] = 0;
		for (long key : opinionTimeLine.keySet()) {
			if (startkey == 0l)
				startkey = key;
			long hours = (key - startkey) / 3600000;
			float hotScore = 0;
			allscore += opinionTimeLine.get(key);
			if(hours == lastpoint[0]){
				lastpoint[1] += hotScore;
			}else{
				if(lastpoint[0] != 0)
					linePoints.add(lastpoint);
				lastpoint = new float[2];
				lastpoint[0] = hours;
				lastpoint[1] = allscore;
			}
			log.debug("lastpoint: "+lastpoint[0]+","+lastpoint[1]);
		}
		linePoints.add(lastpoint);
		return linePoints;
	}
	
	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getEventhot() {
		return eventhot;
	}

	public void setEventhot(int eventhot) {
		this.eventhot = eventhot;
	}

	public static void main(String args[]){
		TreeMap<Long, Integer> opinionTimeLine = new TreeMap<Long, Integer>();
		for(int i=0;i<10;i++){
			opinionTimeLine.put(System.currentTimeMillis()-i*3600000, i*2);
		}
		EventHotModel  m = new EventHotModel(opinionTimeLine);
		System.out.println(m.getEventhot());
		System.out.println(m.getRate());
		System.out.println(m.getLinePoints());
		System.out.println(m.getLevel());
	}

}
