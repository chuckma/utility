package com.shishu.utility.file;

import java.io.IOException;

public class ZIPUtilsTest {
	
	
	public static void main(String[] args) throws IOException {
		String content = "顶顶顶顶顶顶顶顶ddddddddddddddddddddddeeeeeeeeeeeeeeeeeeeeeeeeee1234!!!!!@#$%^%&*()";
		System.out.println(content.length());
		ZIPUtils.zip(content,"/home/jingzhong/hello.zip");
		
	}

}
