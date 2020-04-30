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

package com.distressed.asset.portal.interceptor;

import com.distressed.asset.common.annotation.DecryptField;
import com.distressed.asset.common.annotation.EncryptField;
import com.distressed.asset.common.utils.AESUtils;
import com.distressed.asset.common.utils.MyStringUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 安全字段加密解密切面。
 *
 * @author hongchao zhao at 2019-12-3 17:31
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
@Component
public class EncryptFieldAop {
    private final Logger logger = LoggerFactory.getLogger(EncryptFieldAop.class);

    @Value("${sys.encrypt.key}")
    private String secretKey;
    @Value("${sys.encrypt.switch}")
    public String encryptSwitch;

    @Pointcut("@annotation(com.distressed.asset.common.annotation.EncryptMethod)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object responseObj = null;
        try {
            //过滤掉空参的方法
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                //切记，当前只取了第一个参数，如果有多个参数，请自行调整
                Object requestObj = joinPoint.getArgs()[0];
                if ("1".equals(encryptSwitch)) {
                    //入参加密
                    handleEncrypt(requestObj);
                }
            }
            responseObj = joinPoint.proceed();
            if ("1".equals(encryptSwitch)) {
                //出参解密
                handleDecrypt(responseObj);
            }

        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("SecureFieldAop处理出现异常【{}】", e.getMessage());
        }
        return responseObj;
    }

    /**
     * 处理加密。
     *
     * @param requestObj 请求对象。
     */
    private void handleEncrypt(Object requestObj) throws Exception {
        if (Objects.isNull(requestObj)) {
            return;
        }

        //logger.info("入参对象.class===>" + requestObj.getClass().getName());
        if (requestObj instanceof Map) {
            //logger.info("Map对象加密。。。");
            Map params = (Map) requestObj;
            //把Map参数中需要加密的字段抽出，因为Map中无法以注解标注
            generateEncryptMap(params);

        } else {
            //logger.info("Java对象加密。。。");
            Field[] fields = requestObj.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean hasSecureField = field.isAnnotationPresent(EncryptField.class);
                if (hasSecureField) {
                    field.setAccessible(true);
                    String plaintextValue = (String) field.get(requestObj);
                    String encryptValue = AESUtils.getInstance().encrypt(secretKey, plaintextValue);
                    field.set(requestObj, encryptValue);
                }
            }
        }
    }

    /**
     * 辅助方法：初始化需要加密的Map入参。
     *
     * @param params Map类型的请求参数。
     */
    @SuppressWarnings("unchecked")
    private void generateEncryptMap(Map params) {
        encryptMap("boundCellphone", params);
        encryptMap("certNumber", params);
        encryptMap("bankAccount", params);
        encryptMap("bankNumber", params);
        encryptMap("realName", params);
    }

    /**
     * 辅助方法：将Map对象中的指定参数加密。
     *
     * @param fieldName 参数名【key】。
     * @param params    Map参数对象。
     */
    private void encryptMap(String fieldName, Map<String, Object> params) {
        if (params.containsKey(fieldName) && null != params.get(fieldName)) {
            String number = params.get(fieldName).toString();
            try {
                if (MyStringUtils.isNotBlank(number)){
                    number = AESUtils.getInstance().encrypt(secretKey, number);
                }
                params.put(fieldName, number);
            } catch (Exception e) {
                logger.error("加密请求参数失败:" + ExceptionUtils.getStackTrace(e));
                params.put(fieldName, number);
            }
        }
    }




    /**
     * 处理解密。
     *
     * @param responseObj 返回对象。
     */
    private void handleDecrypt(Object responseObj) throws Exception {
        if (Objects.isNull(responseObj)) {
            return;
        }

        //logger.info("返回结果.class===>" + responseObj.getClass().getName());
        if (responseObj instanceof PageInfo) {
            //logger.debug("返回分页PageInfo对象");
            //不晓得为毛，貌似取不到父类的数据对象，这里手动取一下
            PageInfo pageInfo = (PageInfo) responseObj;
            List page = pageInfo.getList();
            generateDecryptList(page);

        } else {
            Field[] fields = responseObj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object encryptData = field.get(responseObj);
                //logger.info("返回结果encryptData.class===>" + encryptData.getClass().getName());
                if (encryptData instanceof ArrayList<?>) {
                    //logger.debug("返回List对象");
                    List<?> list = (ArrayList<?>) encryptData;
                    generateDecryptList(list);

                } else if (encryptData instanceof Map) {
                    //logger.debug("返回Map对象");
                    Map map = (HashMap) encryptData;
                    generateDecryptMap(map);
                } else {
                    //logger.debug("返回Object对象");
                    if (encryptData!=null){
                        Field[] dataFields = encryptData.getClass().getDeclaredFields();
                        for (Field data : dataFields) {
                            toDecrypt(encryptData, data);
                        }
                    }
                }
            }
        }

    }

    /**
     * 辅助方法：处理返回的List对象。
     *
     * @param list List对象。
     * @throws Exception 加解密异常。
     */
    private void generateDecryptList(List<?> list) throws Exception {
        for (Object val : list) {
            if (val instanceof HashMap) {
                //logger.debug("返回List<Map>对象");
                Map map = (HashMap) val;
                generateDecryptMap(map);

            } else if (val != null) {
                //logger.debug("返回List<Object>对象");
                Field[] dataFields = val.getClass().getDeclaredFields();
                for (Field data : dataFields) {
                    toDecrypt(val, data);
                }
            }
        }
    }

    /**
     * 辅助方法：将返回结果为Map对象的数据中，需要解密的数据进行解密。
     *
     * @param map Map类型的返回对象。
     */
    @SuppressWarnings("unchecked")
    private void generateDecryptMap(Map map) {
        decryptMap("boundCellphone", map);
        decryptMap("certNumber", map);
        decryptMap("bankAccount", map);
        decryptMap("bankNumber", map);
        decryptMap("realName", map);
    }

    /**
     * 辅助方法：解密Map对象指定的加密数据。
     *
     * @param fieldName 结果名【key】。
     * @param params    Map结果对象。
     */
    private void decryptMap(String fieldName, Map<String, Object> params) {
        if (params.containsKey(fieldName) && null != params.get(fieldName)) {
            String number = params.get(fieldName).toString();
            try {
                //32是因为AES加密字符串最少有32位长度，避免未解密未加密的字符串报错
                if (number != null && number.length() >= 32) {
                    number = AESUtils.getInstance().decrypt(secretKey, number);
                }
                params.put(fieldName, number);
            } catch (Exception e) {
                logger.error("解密结果参数失败:" + ExceptionUtils.getStackTrace(e));
                params.put(fieldName, number);
            }
        }
    }

    /**
     * 辅助方法：将加密字段解密。
     *
     * @param responseObj 加密对象。
     * @param field       加密类型。
     */
    private void toDecrypt(Object responseObj, Field field) throws Exception {
        boolean hasSecureField = field.isAnnotationPresent(DecryptField.class);
        if (hasSecureField) {
            field.setAccessible(true);
            String encryptValue = (String) field.get(responseObj);
            if (encryptValue != null && encryptValue.length() >= 32) {
                encryptValue = AESUtils.getInstance().decrypt(secretKey, encryptValue);
            }
            field.set(responseObj, encryptValue);
        }
    }

}
