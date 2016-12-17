package com.shishu.utility.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.imgscalr.Scalr;
import org.slf4j.Logger;

import com.shishu.utility.log.LogUtil;

public class ThumbnailGenerator {
	
	private static Logger log = LogUtil.getLogger(ThumbnailGenerator.class);

	public static void main(String[] args) {
		File inputDir = new File("/home/jingzhong/Desktop/logs");
		File outputDir = new File("/home/jingzhong/Desktop/logs2");
		outputDir.mkdir();
		//Change target size here
		int targetSize = 172;

		// Process each file inside the originals/ directory (sub-directories
		// are ignored)
		for (File img : inputDir.listFiles()) {
			if (img.isFile())
				process(img, outputDir, targetSize);
		}

	}
	
	public static byte[] getThumbnailBytes(byte[] imageBytes, int width, int height, String format, String url) {
		int targetSize = width;
		InputStream in = new ByteArrayInputStream(imageBytes);
		BufferedImage source;
		ByteArrayOutputStream baos = null;
		byte[] imageInByte = null;
		try {
			BufferedImage output = null;
			source = ImageIO.read(in);
			if(source == null) {
				log.error("ERROR::image is null. " + url);
				return null;
			}
			if(source.getWidth() > targetSize) {
				
				output = Scalr.resize(source, targetSize);
				
				for(int i = 0;i < 100 ; i++ ) { //循环增加宽度值，使高度值与设定目标相近，最多迭代100次
					if(output.getHeight() < height - 2) {
						targetSize = targetSize + 5;
						output = Scalr.resize(source, targetSize);
					} else {
						break;
					}
				}
				
			} else {
				output = source;
			}
			
			
			boolean hasAlpha = source.getColorModel().hasAlpha();
			if (hasAlpha) {
				log.debug("Has alpha (i.e. transparency):" + hasAlpha);
				output = dropAlphaChannel(output);
			}
			
			int sourceW = source.getWidth();
			int sourceH = source.getHeight();

			int w = output.getWidth();
			int h = output.getHeight();
			
			if(h > height) {
				output = output.getSubimage(0, 0, output.getWidth(), height);
			}
			
			if(w > width) {
				output = output.getSubimage(0, 0, width, output.getHeight());
			}
			
			
			baos = new ByteArrayOutputStream();
			ImageIO.write(output, format, baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			
			log.debug("Source width,height:" + sourceW + "," + sourceH + " Thumbnail width,height:" + output.getWidth() + "," + output.getHeight() + "::" + url);

		} catch (IOException e) {
			log.error("ThumbnailGenerator-getThumbnailBytes:" + e.getMessage() + " :" + url);
		} finally {
			try {
				if(baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage() + " :" + url);
			}
		}
		
		return imageInByte;
		
	}

	private static void process(File inputFile, File baseDir, int targetSize) {

		// The output name e.g. 'test.jpg' will be the same as the original
		String name = inputFile.getName();
		File outputFile = new File(baseDir, name);
		createThumbnail2(inputFile, outputFile, targetSize);
	}

	private static void createThumbnail2(File imageFile, File outputFile, int targetSize) {
		System.out.println("Processing " + imageFile);
		BufferedImage source;
		try {
			source = ImageIO.read(imageFile);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(source, "png", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			
			byte[] test = getThumbnailBytes(imageInByte,172,120,"png","");
			
			InputStream in = new ByteArrayInputStream(test);
			BufferedImage output = ImageIO.read(in);
			
			writeImage(outputFile, output);
			
			System.out.println("Output file:" + outputFile + ", file size : " + outputFile.length() / 1024 + " KB\n");
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return;
		}
	}
	
	private static void createThumbnail(File imageFile, File outputFile, int targetSize) {
		System.out.println("Processing " + imageFile);
		BufferedImage source;
		try {
			source = ImageIO.read(imageFile);
			BufferedImage output = Scalr.resize(source, targetSize);
			
			for(int i = 0;i < 100 ; i++ ) {
				if(output.getHeight() < 118) {
					targetSize = targetSize + 5;
					output = Scalr.resize(source, targetSize);
				} else {
					break;
				}
			}
			
			boolean hasAlpha = source.getColorModel().hasAlpha();
			if (hasAlpha) {
				System.out.println("Has alpha (i.e. transparency):" + hasAlpha);
				output = dropAlphaChannel(output);
			}

			int sourceW = source.getWidth();
			int sourceH = source.getHeight();

			int w = output.getWidth();
			int h = output.getHeight();
			
			if(h > 120) {
				output = output.getSubimage(0, 0, output.getWidth(), 120);
			}
			
			if(w > 172) {
				output = output.getSubimage(0, 0, 172, output.getHeight());
			}

			writeImage(outputFile, output);
			System.out.println("Source width,height : " + sourceW + "," + sourceH);
			System.out.println("Output width,height : " + output.getWidth() + "," + output.getHeight());
			System.out.println("Output file:" + outputFile + ", file size : " + outputFile.length() / 1024 + " KB\n");
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return;
		}
	}

	/**
	 * Writes out an image to the file using the file extension to determine the
	 * image format.
	 * 
	 * @param outputFile
	 *            the target file. The filetype is determined by the extension
	 *            e.g. png, jpg
	 * @param image
	 *            the image to save
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void writeImage(File outputFile, BufferedImage image) throws FileNotFoundException, IOException {

		// A lot of 'boilerplate' code to ask Java to save an image!

		// Extract the file extension e.g. 'jpg' 'png'
		String name = outputFile.getName().toLowerCase();
		String suffix = name.substring(name.lastIndexOf('.') + 1);

		boolean isJPG = suffix.endsWith("jpg");

		Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(suffix);
		if (!writers.hasNext())
			System.err.println("Unrecognized image file extension " + suffix);

		// Check we can create a new, empty output file
		outputFile.createNewFile();

		ImageWriter writer = writers.next();
		writer.setOutput(new FileImageOutputStream(outputFile));

		ImageWriteParam param = writer.getDefaultWriteParam();
		// png files don't support compression and will throw an exception if we
		// try to set compression mode
		if (isJPG) {
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.76f); // High quality
		}
		IIOImage iioImage = new IIOImage(image, null, null);
		writer.write(null, iioImage, param);
	}

	/**
	 * Drops alpha channel which may give a pink background for the output image.
	 * 
	 * @param src
	 * @return
	 */
	public static BufferedImage dropAlphaChannel(BufferedImage src) {
		BufferedImage convertedImg = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		convertedImg.getGraphics().drawImage(src, 0, 0, null);
		return convertedImg;
	}
}
