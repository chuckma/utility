package com.shishu.utility.hotmodel;

public class OpinionHotModel {
	/**
	 * 获取单条舆情热度
	 * @author wgh
	 * @param numClick
	 * @param numComment
	 * @param siteRank
	 * @return
	 */
    public static int getOpinionModel(int numClick,int numComment,String siteRank){
    	return (int) ((1+Math.log(1 + numClick + numComment*100)) * getSiteScore(siteRank));
    }
    
    private static int getSiteScore(String siteRank){
    	if(siteRank == null) return 1;
    	switch(siteRank){
    	case "A":
    		return 5;
    	case "B":
    		return 4;
    	case "C":
    		return 3;
    	case "D":
    		return 2;
    	default :
    		return 1;
    		
    	}
    }
}
