package com.distressed.asset.common.utils;

import com.distressed.asset.common.annotation.EncryptField;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;

/**
 * java使用AES加密解密 AES-128-ECB加密
 * 与mysql数据库aes加密算法通用，to_base64只适用mysql5.6之后的，之前的没有这个函数，不适用，可以使用HEX，UNHEX；
 * 数据库aes加密解密
 * -- 加密
 * SELECT to_base64(AES_ENCRYPT('www.gowhere.so','jkl;POIU1234++=='));
 * -- 解密
 * SELECT AES_DECRYPT(from_base64('Oa1NPBSarXrPH8wqSRhh3g=='),'jkl;POIU1234++==');
 * <p>
 * 16进制的。【目前我们的数据库是5.5.4，采用的是16进制的，结果一样】
 * SELECT HEX(AES_ENCRYPT("18665958561",'zwyhAESC20191202'));
 * SELECT AES_DECRYPT(unhex('6CE459766900371A5155FA7BC7CA94ED'),'zwyhAESC20191202');
 *
 * @author zhaohc
 */
@Component
public class AESUtils {

    protected static Logger logger = LoggerFactory.getLogger(AESUtils.class);


    private static final String ENCRYPT_TYPE = "AES";
    private static final String ENCODING = "UTF-8";

    private static AESUtils aesUtils;
    /**
     * 加密cipher
     */
    private static Cipher encryptCipher;
    /**
     * 解密chipher
     */
    private static Cipher decryptChipher;

    private Integer isDecryptInit = 0;
    private Integer isEncryptInit = 0;

    /**
     * encryptCipher、decryptChipher初始化
     */
    private AESUtils() {
    }

    public static void init() {
        try {
            encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            decryptChipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    // 加密
    public String encrypt(String sKey, String sSrc) throws Exception {
        if (null == sKey) {
            logger.error("Key为空n");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            logger.error("Key长度不是16位");
            return null;
        }
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        if (isEncryptInit == 0) {
            encryptCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sKey.getBytes(ENCODING), ENCRYPT_TYPE));
            isEncryptInit = 1;
        }
        return parseByte2HexStr(encryptCipher.doFinal(sSrc.getBytes(ENCODING)));
    }

    // 解密
    public String decrypt(String sKey, String sSrc) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                logger.error("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                logger.error("Key长度不是16位");
                return null;
            }
            //先用base64解密
            if (isDecryptInit == 0) {
                decryptChipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sKey.getBytes(ENCODING), ENCRYPT_TYPE));
                isDecryptInit = 1;
            }
            byte[] encrypted1 = parseHexStr2Byte(sSrc);
            assert encrypted1 != null;
            byte[] original = decryptChipher.doFinal(encrypted1);
            return new String(original, ENCODING);
        } catch (Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            return sSrc;
        }
    }

    /**
     * 对含注解字段加密。
     *
     * @param t   加密对象。
     * @param <T> 加密后对象。
     */
    public static <T> void encryptField(String key, T t) {
        try {
            Field[] declaredFields = t.getClass().getDeclaredFields();
            if (declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(EncryptField.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String fieldValue = null;

                        fieldValue = (String) field.get(t);
                        if (StringUtils.isNotEmpty(fieldValue)) {
                            //field.set(t, DESedeUtils.encrypt(key, fieldValue));
                            field.set(t, AESUtils.getInstance().encrypt(key, fieldValue));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单例。
     *
     * @return 加解密工具单例对象。
     */
    public static AESUtils getInstance() {
        if (aesUtils == null) {
            // 当需要创建的时候在加锁
            synchronized (AESUtils.class) {
                if (aesUtils == null) {
                    aesUtils = new AESUtils();
                    init();
                }
            }
        }
        return aesUtils;
    }

    /**
     * 将16进制转换为二进制。
     *
     * @param hexStr 16进制字符串。
     * @return 二进制数据。
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 辅助方法：将二进制转换成16进制。
     *
     * @param buf 二进制数据。
     * @return 16进制字符串。
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */
        String cKey = "zwyhAESC20191202";
        // 需要加密的字串
        String cSrc = "18665958561";
        System.out.println(cSrc);
        // 加密
        String enString = AESUtils.getInstance().encrypt(cKey, cSrc);
        System.out.println("加密后的字串是：" + enString);
        // 解密
        String DeString = AESUtils.getInstance().decrypt(cKey, enString);
        System.out.println("解密后的字串是：" + DeString);

    }
}