/*************************************************************************
 *          HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *          COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS, IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 * DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *          HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.common.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * JSON序列化和反序列化辅助方法集合。
 *
 * @author Yelin.G at 2013-04-17 11:50
 *
 * @see ObjectMapper
 * @see JsonFactory
 */
public class JSONUtils {

    private JSONUtils() {
        super();
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * {@link java.util.Map Map}-><a href="http://www.json.org/">JSON</a>格式的字符串。
     *
     * @param target {@link java.util.Map Map}实例。
     * @param <T> 泛型声明。
     * @return JSON格式字符串。
     * @throws IOException {@link JsonFactory#createGenerator(java.io.Writer)}可能抛出的例外。
     * @see JsonFactory#createGenerator(java.io.Writer)
     * @see ObjectMapper#writeValue(com.fasterxml.jackson.core.JsonGenerator, Object)
     */
    public static <T> String toJSON(Object target) throws IOException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(new JsonFactory().createGenerator(writer), target);
        return writer.toString();
    }

    /**
     * <a href="http://www.json.org/">JSON</a>格式的字符串->{@code <T>}对象。
     *
     * @param JSON JSON格式字符串。
     * @param clazz 要转换对象的类类型。
     * @param <T> 泛型声明。
     * @return 指定类类型的对象实例。
     * @throws IOException {@link ObjectMapper#readValue(String, Class)}可能抛出的例外。
     */
    public static <T> T toObject(String JSON, Class<T> clazz) throws IOException {
        return mapper.readValue(JSON, clazz);
    }
}
