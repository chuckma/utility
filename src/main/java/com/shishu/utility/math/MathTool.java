package com.shishu.utility.math;

import java.math.BigDecimal;

public class MathTool {
	public static float getFloatCut(float d,int numcut) {
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(numcut, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
	
	public static void main(String args[]){
		System.out.println(getFloatCut(3.1062259674072266f,2));
	}

}
