/*************************************************************************
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *                COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED  SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS,IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.common.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

/**
 * 文件处理工具类。
 *
 * @author hongchao zhao at 2019-11-20 11:28
 */
public class FileUtils {

    /**
     * 将文件流在批定位置生成对应的文件。
     *
     * @param file 文件流。
     * @param filePath 保存路径。
     * @param fileName 文件名。
     * @return 保存地址。
     */
    public static String saveFile(byte[] file, String filePath, String fileName) {
        int random = (int) (Math.random() * 100 + 1);
        int random1 = (int) (Math.random() * 100 + 1);
        filePath = filePath + random + File.separator + random1 + File.separator;
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath + fileName);
            FileChannel fileChannel = fileOutputStream.getChannel();
            ByteBuffer buf = ByteBuffer.wrap(file);
            while (fileChannel.write(buf) != 0) {
            }
        } catch (Exception e) {

        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //url
        return random + "/" + random1 + "/" + fileName;
    }

    /**
     * 指定位置开始写入文件
     *
     * @param tempFile  输入文件
     * @param outPath  输出文件的路径(路径+文件名)
     * @throws IOException
     */
    public static void copyFile( String outPath,File tempFile) throws IOException {
        RandomAccessFile raFile = null;
        BufferedInputStream inputStream=null;
        try{
            File dirFile = new File(outPath);
            //以读写的方式打开目标文件
            raFile = new RandomAccessFile(dirFile, "rw");
            raFile.seek(raFile.length());
            inputStream = new BufferedInputStream(new FileInputStream(tempFile));
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                raFile.write(buf, 0, length);
            }
        }catch(Exception e){
            throw new IOException(e.getMessage());
        }finally{
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (raFile != null) {
                    raFile.close();
                }
            }catch(Exception e){
                throw new IOException(e.getMessage());
            }
        }
    }

    /**
     * 将输入流{@code source}保存到目标文件种。
     *
     * @param source {@link File}。
     * @param absoluteTargetFileName 绝对路径文件名。
     * @throws IOException
     */
    public static void copyStream(File source, String absoluteTargetFileName) throws IOException {
        FileInputStream inStream = new FileInputStream(source);
        try {
            copyStream(inStream, absoluteTargetFileName);
        } finally {
            IOUtils.closeQuietly(inStream);
        }
    }


    /**
     * 将输入流{@code source}保存到目标文件种。
     *
     * @param source {@link byte[]}。
     * @param absoluteTargetFileName 绝对路径文件名。
     * @throws IOException
     */
    public static void copyStream(byte[] source, String absoluteTargetFileName) throws IOException {
        InputStream inStream = new ByteArrayInputStream(source);
        try {
            copyStream(inStream, absoluteTargetFileName);
        } finally {
            IOUtils.closeQuietly(inStream);
        }
    }

    /**
     * 将输入流{@code source}保存到目标文件种。
     *
     * @param source {@link InputStream}。
     * @param absoluteTargetFileName 绝对路径文件名。
     * @throws IOException
     */
    public static void copyStream(InputStream source, String absoluteTargetFileName) throws IOException {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(absoluteTargetFileName);
            IOUtils.copy(source, outStream);
        } finally {
            IOUtils.closeQuietly(outStream);
        }
    }

    /**
     * 根据文件路径删除相应文件。
     *
     * @param fileName 文件路径。
     * @return 删除结果。
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    public static boolean makeDirectoryByDate(){

        return true;
    }

    /**
     * 根据文件名重新随机生成UUID名称。
     *
     * @param fileName 文件名。
     * @return 新文件名。
     */
    public static String renameToUUID(String fileName) {
        return GUIDUtils.makeUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件后缀名。
     *
     * @param fileName 文件名。
     * @return 后缀名。
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    public static void main(String[] args) {
        System.out.println(getSuffix("d:/myImg/001.png"));
    }


    /**
     *
     * @param size
     * @return
     */
    public static String getPrintSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (size == 0) {
            return wrongSize;
        }
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
