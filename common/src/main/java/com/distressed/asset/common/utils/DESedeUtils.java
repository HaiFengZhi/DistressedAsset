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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * DESede是由DES对称加密算法改进后的一种对称加密算法。使用 168 位的密钥对资料进行三次加密的一种机制；
 * 它通常（但非始终）提供极其强大的安全性。如果三个 56 位的子元素都相同，则三重 DES 向后兼容 DES。
 *
 * <p>
 *     在SSO交互的过程中，我们对用户认证信息，比如说TICKET(跨域时)和TOKEN，进行加密解密时通过DESede
 *     方式在客户端和服务端进行加解密。
 * </p>
 *
 * @author Yelin.G at 2015/07/28
 */
public final class DESedeUtils {

    public DESedeUtils() {
        super();
    }

    /**
     * DESede加解密模式枚举。
     */
    public enum DESedeMode {
        ENCRYPT_MODE(Cipher.ENCRYPT_MODE),
        DECRYPT_MODE(Cipher.DECRYPT_MODE);

        int mode;
        DESedeMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return this.mode;
        }
    }

	private static final String ALGORITHM = "DESede";
    private static final String ALGORITHM_CBC = "DES/CBC/PKCS5Padding";
    private static final String DEFAULT_ENCRYPT_KEY = "mcfsoft ";   //老平台默认加密秘钥。

    /**
     * 通过DESede算法利用<code>key</code>对数据<code>data</code>进行加密。
     *
     * <p>
     *     使用DESede加密算法加密出来的数据是二进制，对二进制采用BASE64编码加密成
     *     字符串。
     * </p>
     *
     * <p>
     *     这个方法可以独立使用，不需要其他方法配合；内部包含了对KEY的初始化。
     * </p>
     *
     * @param key 加密KEY。
     * @param data 待加密的数据。
     * @return 加密之后的字符串。
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @see #decrypt(String, String)
     */
    public static synchronized String encrypt(String key, String data) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidKeySpecException,
            IllegalBlockSizeException,
            BadPaddingException {
        if (CommonUtils.isBlank(key, data)) {
            throw new IllegalArgumentException("DESede加密KEY和数据不能为空。");
        }

        Cipher cipher = init(DESedeMode.ENCRYPT_MODE, key);
        byte[] encData = cipher.doFinal(data.getBytes(Charset.forName("utf8")));
        return CryptographUtils.convertHexBytesToString(encData);
    }

    /**
     * 通过DESede算法利用{@link Cipher}对数据<code>data</code>进行加密。
     *
     * <p>
     *     这里的{@link Cipher}对象是通过<code>key</code>以及{@link Cipher#ENCRYPT_MODE}初始化
     *     后的密码对象；所以这个方法要和{@link #init(DESedeMode, String)}配合使用，举例：
     *     String key = ...;
     *     String data = ...;
     *     Cipher cipher = DESedeUtils.init(Cipher.ENCRYPT_MODE, key);
     *     String result = DESedeUtils.encrypt(cipher, data);
     * </p>
     *
     * @param cipher {@link Cipher}密码对象，是通过{@link Cipher#ENCRYPT_MODE}和<code>key</code>初始化后的密码对象。
     * @param data 待加密的字符串。
     * @return 加密后的字符串。
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @see #init(DESedeMode, String)
     * @see #decrypt(Cipher, String)
     */
    public static synchronized String encrypt(Cipher cipher, String data) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidKeySpecException,
            IllegalBlockSizeException,
            BadPaddingException {
        if (CommonUtils.isBlank(cipher, data)) {
            throw new IllegalArgumentException("DESede加密Cipher和数据不能为空。");
        }

        byte[] encData = cipher.doFinal(data.getBytes(Charset.forName("utf8")));
        return CryptographUtils.convertHexBytesToString(encData);
    }

    /**
     * 通过DESede算法利用<code>key</code>对数据<code>data</code>进行解密。
     *
     * <p>
     *     解密之前，要先对字符串数据采用BASE64编码解密成二进制，再转换成UTF8编码
     *     的字符串。
     * </p>
     *
     * <p>
     *     这个方法可以独立使用，不需要其他方法配合；内部包含了对KEY的初始化。
     * </p>
     *
     * @param key 解密KEY。
     * @param data 待解密的数据，BASE6编码的字符串。
     * @return 加密之后的字符串。
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @see #encrypt(String, String)
     */
    public static synchronized String decrypt(String key, String data) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidKeySpecException,
            IllegalBlockSizeException,
            BadPaddingException {
        if (CommonUtils.isBlank(key, data)) {
            throw new IllegalArgumentException("DESede解密KEY和数据不能为空。");
        }

        Cipher cipher = init(DESedeMode.DECRYPT_MODE, key);
        byte[] decData = cipher.doFinal(CryptographUtils.convertStringToHexBytes(data));
        return new String(decData, Charset.forName("utf8"));
    }

    /**
     * 通过DESede算法利用{@link Cipher}对数据<code>data</code>进行解密。
     *
     * <p>
     *     这里的{@link Cipher}对象是通过<code>key</code>以及{@link Cipher#DECRYPT_MODE}初始化
     *     后的密码对象；所以这个方法要和{@link #init(DESedeMode, String)}配合使用，举例：
     *     String key = ...;
     *     String dataEncodedByBase64 = ...;
     *     Cipher cipher = DESedeUtils.init(Cipher.DECRYPT_MODE, key);
     *     String result = DESedeUtils.decrypt（cipher, dataEncodedByBase64);
     * </p>
     *
     * @param cipher {@link Cipher}密码对象，是通过{@link Cipher#DECRYPT_MODE}和<code>key</code>初始化后的密码对象。
     * @param data 待解密的BASE64编码的字符串。
     * @return 解密后的字符串。
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @see #init(DESedeMode, String)
     * @see #encrypt(Cipher, String) c
     */
    public static synchronized String decrypt(Cipher cipher, String data) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidKeySpecException,
            IllegalBlockSizeException,
            BadPaddingException {
        if (CommonUtils.isBlank(cipher, data)) {
            throw new IllegalArgumentException("DESede解密Cipher和数据不能为空。");
        }

        byte[] decData = cipher.doFinal(CryptographUtils.convertStringToHexBytes(data));
        return new String(decData, Charset.forName("utf8"));
    }

    /**
     * 通过{@link DESedeMode}和KEY初始化密码对象，用于加密
     * 或者解密。
     *
     * @param mode {@link DESedeMode}枚举，加密或者解密。
     * @param key 加密键。
     * @return {@link Cipher}，初始化后的密码对象。
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static Cipher init(DESedeMode mode, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        if (CommonUtils.isBlank(key)) {
            throw new IllegalArgumentException("DESede加密KEY不能为空。");
        }

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        Key secKey = generateSecretKey(key);
        cipher.init(mode.getMode(), secKey, new SecureRandom());
        return cipher;
    }

    /**
     * 通过秘钥KEY初始化密码对象，用于加密
     * 或者解密。
     *
     * @param key 加密键。
     * @return {@link Cipher}，初始化后的密码对象。
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static Cipher initByCBC(DESedeMode mode, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_CBC);

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(mode.getMode(), secretKey, iv);

        return cipher;
    }

    /**
     * 辅助方法：通过KEY生成{@link Key}对象，用于密码对象的初始化。
     *
     * <p>
     *     密钥字符串是BASE64编码，使用之前要进行解码。
     * </p>
     *
     * @param key 密钥字符串，BASE64编码，使用之前要进行解码。
     * @return {@link Key}对象。
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static Key generateSecretKey(String key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] material = Arrays.copyOf(Base64.decodeBase64(key.getBytes(Charset.forName("utf8"))), 24);
        DESedeKeySpec keySpec = new DESedeKeySpec(material);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(keySpec);
    }

    /**
     * 通过CBC方式的DES解密十六进制串(此方法与老平台同步)。
     *
     * @param decryptStr 待解密串。
     * @param key        秘钥。
     * @return 解密后原字符串。
     * @throws Exception
     */
    public static String decryptByCBC(String decryptStr, String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            key = DEFAULT_ENCRYPT_KEY;
        }
        byte[] byteSrc = CryptographUtils.convertStringToHexBytes(decryptStr);
        Cipher cipher = initByCBC(DESedeMode.DECRYPT_MODE, key);

        byte[] retByte = cipher.doFinal(byteSrc);
        return URLDecoder.decode(new String(retByte, Charsets.UTF_8), "UTF-8");
    }

    /**
     * 通过CBC方式的DES加密，然后进行十六进制转换(此方法与老平台同步)。
     *
     * @param encryptStr 待加密传。
     * @param key        秘钥。
     * @return 加密后字符串转。
     * @throws Exception
     */
    public static String encryptByCBC(String encryptStr, String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            key = DEFAULT_ENCRYPT_KEY;
        }
        encryptStr = URLEncoder.encode(encryptStr, "UTF-8");
        Cipher cipher = initByCBC(DESedeMode.ENCRYPT_MODE, key);

        return CryptographUtils.convertHexBytesToString(cipher.doFinal(encryptStr.getBytes("UTF-8"))).toUpperCase();
    }

    /**
     * DES算法密钥
     */
    private static final byte[] DES_KEY = { 21, 1, -110, 82, -32, -85, -128, -65 };

    /**
     * 数据加密，算法（DES）
     *
     * @param data
     *            要进行加密的数据
     * @return 加密后的数据
     */
    @SuppressWarnings("restriction")
    public static String encryptBasedDes(String data) {
        String encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);
            // 加密，并把字节数组编码成字符串
            encryptedData = new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            // log.error("加密错误，错误信息：", e);
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    /**
     * 数据解密，算法（DES）
     *
     * @param cryptData
     *            加密数据
     * @return 解密后的数据
     */
    @SuppressWarnings("restriction")
    public static String decryptBasedDes(String cryptData) {
        String decryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(DES_KEY);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 解密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            // 把字符串解码为字节数组，并解密
            decryptedData = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));
        } catch (Exception e) {
            // log.error("解密错误，错误信息：", e);
            throw new RuntimeException("解密错误，错误信息：", e);
        }
        return decryptedData;
    }

    public static void main(String[] args) throws Exception {
        String data = "distress-admin";
        String entry = encryptBasedDes(data);
        String decrypt = decryptBasedDes(entry);
        System.out.println("加密前：" + data);
        System.out.println("加密后：" + entry);
        System.out.println("解密后：" + decrypt);

        String[] perms = new String[]{"add","delete","update"};
        System.out.println(Arrays.toString(perms));


    }
}
