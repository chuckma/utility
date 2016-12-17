package com.shishu.utility.image;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import com.shishu.utility.log.LogUtil;

public class IMGUtil {
	
	private static Logger log = LogUtil.getLogger(IMGUtil.class);
	
	public static String ImageFormat = "jpg,gif,png,bmp,jpeg";

	public static String getFormatName(Object object) throws Exception {
		ImageInputStream iis = ImageIO.createImageInputStream(object);
		Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);
		while (iterator.hasNext()) {
			ImageReader reader = (ImageReader) iterator.next();
			return reader.getFormatName();
		}

		return null;
	}
	
	
	public static String getImageFomat(String url, byte[] bytes) {
		String ext = ImageFormat;
		String[] exts = ext.split(",");
		String ret = null;
		for(String format : exts) {
			if(url.toLowerCase().endsWith("." + format) || url.toLowerCase().contains("." + format)) {
				ret = format;
				break;
			}
		}
		if(ret == null) {
			InputStream input = new ByteArrayInputStream(bytes);
			try {
				ret = IMGUtil.getFormatName(input);
			} catch (Exception e) {
				log.error("ImageUtil.getFormatName Errors:" + e.getMessage());
			}
		}
		log.info("Image Format: " + ret + " :" + url);
		return ret;
	}
	
	public static boolean isImage(String url) {
		String ext = ImageFormat;
		String[] exts = ext.split(",");
		boolean ret = false;
		for(String format : exts) {
			if(url.toLowerCase().endsWith(format) || url.toLowerCase().contains("." + format)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public static String getStringFromImage(byte[] image) {
		byte[] encodedBytes = Base64.encodeBase64(image);
		return new String(encodedBytes);
	}
	
	public static byte[] getBtyesFromBase64Str(String baseStr) {
		byte[] decodedBytes = Base64.decodeBase64(baseStr.getBytes());
		return decodedBytes;
	}

}
