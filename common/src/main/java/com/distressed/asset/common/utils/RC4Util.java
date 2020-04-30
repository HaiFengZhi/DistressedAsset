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

import java.io.UnsupportedEncodingException;

/**
 * RC4加密解密工具类。
 *
 * <p>
 * RC4是一种流加密算法，密钥长度可变。它加解密使用相同的密钥，因此也属于对称加密算法。
 * </p>
 *
 * @author hongchao zhao at 2019-11-29 16:54
 */
public class RC4Util {

    /**
     * RC4加密，将加密后的数据进行哈希
     *
     * @param data     需要加密的数据
     * @param key      加密密钥
     * @param chartSet 编码方式
     * @return 返回加密后的数据
     * @throws UnsupportedEncodingException
     */
    public static String encryRC4String(String data, String key, String chartSet) throws UnsupportedEncodingException {
        if (data == null || key == null) {
            return null;
        }
        return bytesToHex(encryRC4Byte(data, key, chartSet));
    }

    /**
     * RC4加密，将加密后的字节数据
     *
     * @param data     需要加密的数据
     * @param key      加密密钥
     * @param chartSet 编码方式
     * @return 返回加密后的数据
     * @throws UnsupportedEncodingException
     */
    public static byte[] encryRC4Byte(String data, String key, String chartSet) throws UnsupportedEncodingException {
        if (data == null || key == null) {
            return null;
        }
        if (chartSet == null || chartSet.isEmpty()) {
            byte bData[] = data.getBytes();
            return RC4Base(bData, key);
        } else {
            byte bData[] = data.getBytes(chartSet);
            return RC4Base(bData, key);
        }
    }

    /**
     * RC4解密
     *
     * @param data     需要解密的数据
     * @param key      加密密钥
     * @param chartSet 编码方式
     * @return 返回解密后的数据
     * @throws UnsupportedEncodingException
     */
    public static String decryRC4(String data, String key, String chartSet) throws UnsupportedEncodingException {
        if (data == null || key == null) {
            return null;
        }
        return new String(RC4Base(hexToByte(data), key), chartSet);
    }

    /**
     * RC4加密初始化密钥
     *
     * @param aKey
     * @return
     */
    private static byte[] initKey(String aKey) {
        byte[] bkey = aKey.getBytes();
        byte state[] = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (bkey.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((bkey[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % bkey.length;
        }
        return state;
    }


    /**
     * 字节数组转十六进制
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 十六进制转字节数组
     *
     * @param inHex
     * @return
     */
    public static byte[] hexToByte(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = (byte) Integer.parseInt(inHex.substring(i, i + 2), 16);
            j++;
        }
        return result;
    }

    /**
     * RC4解密
     *
     * @param input
     * @param mKkey
     * @return
     */
    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte key[] = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String encryStr = RC4Util.encryRC4String("测试", "123456","UTF-8");
        System.out.println("加密后得到得字符串："+encryStr);
        String decryStr = RC4Util.decryRC4(encryStr, "123456","UTF-8");
        System.out.println("解密后得到得字符串："+decryStr);


    }
}
