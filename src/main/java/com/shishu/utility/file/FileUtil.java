package com.shishu.utility.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.shishu.utility.log.LogUtil;

public class FileUtil {
	
	private static Logger log = LogUtil.getLogger(FileUtil.class);
	
	/**
	 * 
	 * @param path
	 * @param code
	 * @throws IOException
	 */
	public static void writeFile(String path, String str)
			throws IOException {
		path = path.replaceAll("\\\\", "/");
		
		String dir = path.substring(0, path.lastIndexOf("/"));
		File dirFile = new File(dir);
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		FileOutputStream fileOut = new FileOutputStream(path);
		OutputStreamWriter fileOutWriter = new OutputStreamWriter(fileOut,"utf-8");
		
		fileOutWriter.write(str);
		fileOutWriter.flush();
		fileOutWriter.close();
		log.info("生成文件 : " + path);
	}
	
	/**
	 * 
	 * @param path
	 * @param code
	 * @throws IOException
	 */
	public static void writeFile(String path, byte[] bytes) {
		path = path.replaceAll("\\\\", "/");
		
		String dir = path.substring(0, path.lastIndexOf("/"));
		File dirFile = new File(dir);
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(path);
			fileOut.write(bytes);  
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if(fileOut != null) {
					fileOut.flush();  
					fileOut.close();
				}
				
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		log.debug("生成文件 : " + path);
	}

	/**
	 * 删除文件
	 * 
	 * @param f
	 * @return
	 */
	public static boolean deletefile(File f){
		if (f.isFile())
			return f.delete();
		return true;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param f
	 * @return
	 */
	public static boolean deletedir(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory())
					deletedir(files[i]);
				else
					deletefile(files[i]);
			}
		}
		f.delete();
		return true;
	}

	/**
	 * 得到指定文件夹下，指定文件夹名前缀的所有文件夹,除了excludeDir
	 * 
	 * @param dir
	 * @param namePrefix
	 * @param excludeDir
	 * @return
	 */
	public static List<File> getDirByNameprefix(File dir, String namePrefix,
			String excludeDir) {
		List<File> dirList = new ArrayList<File>();
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (!files[i].isDirectory()
						|| files[i].getName().equals(excludeDir))
					continue;
				if (files[i].getName().startsWith(namePrefix))
					dirList.add(files[i]);
			}
		}
		return dirList;
	}

	/**
	 * 文件重命名
	 * 
	 * @param oldFile
	 * @param newFileName
	 * @return
	 */
	public static boolean renameFile(File oldFile, String newFileName) {
		if (oldFile.isFile()) {
			oldFile.renameTo(new File(oldFile.getAbsolutePath().substring(0,
					oldFile.getAbsolutePath().lastIndexOf(File.separator) + 1)
					+ newFileName));
		}
		return true;
	}

	/**
	 * 文件夹重命名
	 * 
	 * @param oldDir
	 * @param newDirName
	 * @return
	 */
	public static boolean renameDir(File oldDir, String newDirName) {
		if (oldDir.isDirectory()) {
			oldDir.renameTo(new File(oldDir.getAbsolutePath().substring(0,
					oldDir.getAbsolutePath().lastIndexOf(File.separator) + 1)
					+ newDirName));
		}
		return true;
	}

	/**
	 * 
	 * @method：copyFile
	 * @param srcFile
	 *            源文件全路径
	 * @param targetFile
	 *            目标文件全路径
	 * @description：将源文件复制一份到目标文件(主要是文件名不同)
	 */
	public static void copyFile(String srcFile, String targetFile) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(srcFile));
			FileOutputStream outputStream = new FileOutputStream(new File(
					targetFile));
			byte[] b = new byte[1024];
			while (inputStream.read(b) != -1) {
				outputStream.write(b);
			}
			inputStream.close();
			outputStream.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// added by yanghb20090116
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				// FileUtils inputStream 关闭错误");
			}
		}
	}


	/**
	 * 
	 * 保存文件
	 * @param file
	 * @param path
	 * @param filename
	 * @throws IOException
	 * @return: void
	 */
	public static void saveFile(File file, String path, String filename) throws IOException {
		
        File folder = new File(path);
        if (!folder.exists()) {
        	folder.mkdirs();
        }
		
		FileInputStream stream = new FileInputStream(file);
		FileOutputStream fs = new FileOutputStream(path + "/" + filename);
		byte[] buffer = new byte[1024 * 1024];
		int bytesum = 0;
		int byteread = 0;
		while ((byteread = stream.read(buffer)) != -1) {
			bytesum += byteread;
			fs.write(buffer, 0, byteread);
			fs.flush();
		}
		fs.close();
		stream.close();
	}
	
	/**
     * 读取文本文件内容
     * @param filePath 文件所在路径
     * @return 文本内容
     * @throws IOException 异常
     */
    public static String readFile2String(String filePath) throws IOException {
    	StringBuffer buffer = new StringBuffer();
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        
        return buffer.toString();
    }

}
