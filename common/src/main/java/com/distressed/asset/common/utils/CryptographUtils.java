/*************************************************************************
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *                COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED  SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS,IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 * DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.common.utils;

import com.distressed.asset.common.exception.ParameterException;
import org.apache.commons.io.Charsets;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.zip.CRC32;

/**
 * 密钥加解密算法类集合。
 *
 * @author Yelin.G at 2015/08/01 7:32
 */
public final class CryptographUtils {

    private CryptographUtils() {
        super();
    }

    /**
     * 通过密钥{@code keyBytes}，对字符串进行DES加密。
     *
     * @param keyBytes     密钥。
     * @param forEncrypted 待加密的字符串。
     * @return 加密后的byte[]。
     */
    public static byte[] encryptByDES(
            byte[] keyBytes,
            String forEncrypted) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeySpecException {
        /*DES算法要求有一个可信任的随机数源*/
        SecureRandom sr = new SecureRandom();
        /*从原始密匙数据创建一个DESKeySpec对象*/
        DESKeySpec dks = new DESKeySpec(keyBytes);
        /*创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象*/
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        /* Cipher对象实际完成加密操作*/
        Cipher cipher = Cipher.getInstance("DES");
        /*用密匙初始化Cipher对象*/
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        /*获取数据并加密*/
        byte data[] = forEncrypted.getBytes(Charsets.UTF_8);
        /*执行加密操作*/
        return cipher.doFinal(data);
    }

    /**
     * 通过密钥{@code keyBytes}，解密数据。
     *
     * @param keyBytes       密钥。
     * @param encryptedBytes 待解密的数据。
     * @return 解密后的字符串。
     */
    public static String decryptByDES(
            byte[] keyBytes,
            byte[] encryptedBytes,
            Charset encoding) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeySpecException {
        /*DES算法要求有一个可信任的随机数源*/
        SecureRandom sr = new SecureRandom();
        /*从原始密匙数据创建一个DESKeySpec对象*/
        DESKeySpec dks = new DESKeySpec(keyBytes);
        /*创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象*/
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        /*Cipher对象实际完成解密操作*/
        Cipher cipher = Cipher.getInstance("DES");
        /*用密匙初始化Cipher对象*/
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        /*执行解密操作*/
        byte[] decryptedData = cipher.doFinal(encryptedBytes);
        return encoding == null ? new String(decryptedData, Charsets.UTF_8) : new String(decryptedData, encoding);
    }

    /**
     * CRC32编码，从{@link byte[]}到long。
     *
     * @param bytes 待编码的字节数组。
     * @return 编码后的long型。
     */
    public static long crc32(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return crc32.getValue();
    }

    /**
     * MD5编码。
     *
     * @param source 待编码的串。
     * @return 编码后的串。
     */
    public static String MD5(String source) {
        return MD5(source.getBytes(Charsets.UTF_8));
    }

    /**
     * MD5编码。
     *
     * @param source 待编码的字节数组。
     * @return 编码后的串。
     */
    public static String MD5(byte[] source) {
        if(source == null || source.length == 0) {
            throw new ParameterException("MD5时字节数组不能为空。");
        }
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(source);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
    }

    /**
     * 将16进制字符串转换为字节数组。
     *
     * @param str 待转换字符串。
     * @return 字节数组。
     */
    public static byte[] convertStringToHexBytes(String str) {
        byte digest[] = new byte[str.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = str.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }

    /**
     * 字节数组转换为16进制字符串。
     *
     * @param hex 待转换字节数组。
     * @return 16进制字符串。
     */
    public static String convertHexBytesToString(byte[] hex) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hex) {
            String plainText = Integer.toHexString(0xff & b);
            if (plainText.length() < 2) {
                plainText = "0" + plainText;
            }
            hexString.append(plainText);
        }

        return hexString.toString();
    }

    /**
     * SHA1编码。
     *
     * @param source 待编码的串。
     * @return 编码后的串。
     */
    public static String SHA1(String source) {
        return SHA1(source.getBytes(Charsets.UTF_8));
    }

    /**
     * SHA1编码。
     * @param source    待编码的二进制串。
     * @return
     */
    public static String SHA1(byte[] source){
        if(source == null || source.length == 0) {
            throw new ParameterException("SHA1时字节数组不能为空。");
        }
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            // 获得SHA1摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("SHA1");
            // 使用指定的字节更新摘要
            mdInst.update(source);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
    }
}
