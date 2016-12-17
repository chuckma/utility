/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shishu.utility.file;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// Commons Logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  A collection of utility methods for working on GZIPed data.
 */
public class GZIPUtils {
  
  private static final Logger LOG = LoggerFactory.getLogger(GZIPUtils.class);
  private static final int EXPECTED_COMPRESSION_RATIO= 5;
  private static final int BUF_SIZE= 4096;

  /**
   * Returns an gunzipped copy of the input array.  If the gzipped
   * input has been truncated or corrupted, a best-effort attempt is
   * made to unzip as much as possible.  If no data can be extracted
   * <code>null</code> is returned.
   */
  public static final byte[] unzipBestEffort(byte[] in) {
    return unzipBestEffort(in, Integer.MAX_VALUE);
  }

  /**
   * Returns an gunzipped copy of the input array, truncated to
   * <code>sizeLimit</code> bytes, if necessary.  If the gzipped input
   * has been truncated or corrupted, a best-effort attempt is made to
   * unzip as much as possible.  If no data can be extracted
   * <code>null</code> is returned.
   */
  public static final byte[] unzipBestEffort(byte[] in, int sizeLimit) {
    try {
      // decompress using GZIPInputStream 
      ByteArrayOutputStream outStream = 
        new ByteArrayOutputStream(EXPECTED_COMPRESSION_RATIO * in.length);

      GZIPInputStream inStream = 
        new GZIPInputStream ( new ByteArrayInputStream(in) );

      byte[] buf = new byte[BUF_SIZE];
      int written = 0;
      while (true) {
        try {
          int size = inStream.read(buf);
          if (size <= 0) 
            break;
          if ((written + size) > sizeLimit) {
            outStream.write(buf, 0, sizeLimit - written);
            break;
          }
          outStream.write(buf, 0, size);
          written+= size;
        } catch (Exception e) {
          break;
        }
      }
      try {
        outStream.close();
      } catch (IOException e) {
      }

      return outStream.toByteArray();

    } catch (IOException e) {
      return null;
    }
  }


  /**
   * Returns an gunzipped copy of the input array.  
   * @throws IOException if the input cannot be properly decompressed
   */
  public static final byte[] unzip(byte[] in) throws IOException {
    // decompress using GZIPInputStream 
    ByteArrayOutputStream outStream = 
      new ByteArrayOutputStream(EXPECTED_COMPRESSION_RATIO * in.length);

    GZIPInputStream inStream = 
      new GZIPInputStream ( new ByteArrayInputStream(in) );

    byte[] buf = new byte[BUF_SIZE];
    while (true) {
      int size = inStream.read(buf);
      if (size <= 0) 
        break;
      outStream.write(buf, 0, size);
    }
    outStream.close();

    return outStream.toByteArray();
  }

  /**
   * Returns an gzipped copy of the input array.
   */
  public static final byte[] zip(byte[] in) {
    try {
      // compress using GZIPOutputStream 
      ByteArrayOutputStream byteOut= 
        new ByteArrayOutputStream(in.length / EXPECTED_COMPRESSION_RATIO);

      GZIPOutputStream outStream= new GZIPOutputStream(byteOut);

      try {
        outStream.write(in);
      } catch (Exception e) {
        LOG.error("Failed to get outStream.write input", e);
      }

      try {
        outStream.close();
      } catch (IOException e) {
        LOG.error("Failed to implement outStream.close", e);
      }

      return byteOut.toByteArray();

    } catch (IOException e) {
        LOG.error("Failed with IOException", e);
      return null;
    }
  }
  
  /**
   * 字符串的压缩
   * @param str 待压缩的字符串
   * @return 返回压缩后的字符串
   * @throws IOException
   */
  public static String compress(String str) throws IOException {
      if (null == str || str.length() <= 0) {
          return str;
      }
      // 创建一个新的输出流
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      // 使用默认缓冲区大小创建新的输出流
      GZIPOutputStream gzip = new GZIPOutputStream(out);
      // 将字节写入此输出流
      gzip.write(str.getBytes("utf-8"));  //因为后台默认字符集有可能是GBK字符集，所以此处需指定一个字符集
      gzip.close();
      // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
      return out.toString("ISO-8859-1");
  }
  
  /**
   * 字符串的解压
   * @param str 对字符串解压
   * @return 返回解压缩后的字符串
   * @throws IOException
   */
 public static String unCompress(String str) throws IOException {
      if (null == str || str.length() <= 0) {
          return str;
      }
      // 创建一个新的输出流
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
      ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
      // 使用默认缓冲区大小创建新的输入流
      GZIPInputStream gzip = new GZIPInputStream(in);
      byte[] buffer = new byte[256];
      int n = 0;

      // 将未压缩数据读入字节数组
      while ((n = gzip.read(buffer)) >= 0){
            out.write(buffer, 0, n);
      }
      // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串 
      return out.toString("utf-8"); 
      }
    
}
