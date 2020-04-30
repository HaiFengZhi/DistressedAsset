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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HTTP类操作方法集合。
 *
 * @author Yelin.G at 2015/07/27
 */
public final class HttpUtils {
    public static final String DEFAULT_CHARSET = "UTF-8";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";

    private HttpUtils() {
        super();
    }

    /**
     * 通过{@link HttpServletRequest}获取远程客户端IP地址。
     *
     * @return 客户端IP地址。
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (null != ip && ip.length() != 0) {
            if ("unknown".equalsIgnoreCase(ip)) {
                ip = null;
            } else {
                String[] ips = ip.split(",");
                for (String tmpIP : ips) {
                    if (!"unknown".equalsIgnoreCase(ip.trim())) {
                        return tmpIP;
                    }
                }
            }
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 构建URL后的查询串。
     * <p/>
     * <p>
     * 比如a=b&c=d等。
     * </p>
     *
     * @param params  参数键值对。
     * @param charset 字符集。
     * @return 查询串。
     * @throws IOException
     */
    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        for (Map.Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }

        return query.toString();
    }

    /**
     * 辅助方法：构建HttpURLConnection请求连接。
     *
     * @param url         URL。
     * @param method      GET/POST/PUT/DELETE等。
     * @param contentType Content-Type。
     * @return {@link HttpURLConnection}。
     * @throws IOException
     */
    private static HttpURLConnection getConnection(URL url, String method, String contentType) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setInstanceFollowRedirects(true);
        conn.setRequestMethod(method);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html,application/json");
        conn.setRequestProperty("User-Agent", "hongling-api-interface");
        conn.setRequestProperty("Content-Type", contentType);

        return conn;
    }

    /**
     * 自定义生成请求
     *
     * @param url             请求地址。
     * @param method          请求方式。
     * @param requestProperty 请求头参数。
     * @return HttpURLConnection    返回请求连接对象
     * @throws IOException
     */
    private static HttpURLConnection getConnection(URL url, String method, Map<String, String> requestProperty) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        for (Map.Entry<String, String> entry : requestProperty.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }

        return conn;
    }

    /**
     * 构建URL。
     *
     * @param strUrl URL地址。
     * @param query  查询串。
     * @return {@link URL}。
     * @throws IOException
     */
    private static URL buildGetUrl(String strUrl, String query) throws IOException {
        URL url = new URL(strUrl);
        if (StringUtils.isEmpty(query)) {
            return url;
        }

        if (StringUtils.isEmpty(url.getQuery())) {
            if (strUrl.endsWith("?")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "?" + query;
            }
        } else {
            if (strUrl.endsWith("&")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "&" + query;
            }
        }

        return new URL(strUrl);
    }

    /**
     * 辅助方法：获取返回值。
     *
     * @param conn {@link HttpURLConnection}。
     * @return 值，{@link String}类型。
     * @throws IOException
     */
    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if (StringUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    /**
     * 辅助方法：获取返回值。
     *
     * @param conn    {@link HttpURLConnection}。
     * @param charset 字符集。
     * @return 值，{@link String}类型。
     * @throws IOException
     */
    protected static String getResponseAsString(HttpURLConnection conn, String charset) throws IOException {
        String contentType = conn.getContentType();
        if (StringUtils.isNotEmpty(contentType)) {
            charset = getResponseCharset(contentType);
        }
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if (StringUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    /**
     * 辅助方法：获取返回值。
     *
     * @param stream  {@link InputStream}。
     * @param charset 字符集。
     * @return 值，{@link String}类型。
     * @throws IOException
     */
    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * 辅助方法：获取响应中的字符集。
     *
     * @param contentType <tt>Content-type</tt>值。
     * @return 字符集。
     */
    private static String getResponseCharset(String contentType) {
        String charset = DEFAULT_CHARSET;

        if (!StringUtils.isEmpty(contentType)) {
            String[] params = contentType.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (!StringUtils.isEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }

        return charset;
    }

    /**
     * 执行HTTP GET请求。
     *
     * @param url    请求地址。
     * @param params 请求参数。
     * @return 响应字符串。
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> params) throws IOException {
        return doGet(url, params, DEFAULT_CHARSET);
    }

    /**
     * 执行HTTP GET请求。
     *
     * @param url     请求地址。
     * @param params  请求参数。
     * @param charset 字符集，如UTF-8, GBK, GB2312。
     * @return 响应字符串。
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> params, String charset) throws IOException {
        return doGet(url, params, charset, 3000);
    }

    /**
     * 执行HTTP GET请求。
     *
     * @param url     请求地址。
     * @param params  请求参数。
     * @param charset 字符集，如UTF-8, GBK, GB2312。
     * @param readTimeout   超时时间，单位毫秒。
     * @return 响应字符串。
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> params, String charset, int readTimeout) throws IOException {
        HttpURLConnection conn = null;

        try {
            String ctype = "application/x-www-form-urlencoded;charset=" + charset;
            String query = buildQuery(params, charset);
            conn = getConnection(buildGetUrl(url, query), METHOD_GET, ctype);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(readTimeout);

            return getResponseAsString(conn);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 执行HTTP GET请求。
     *
     * @param url     请求地址。
     * @param params  请求参数。
     * @param charset 字符集，如UTF-8, GBK, GB2312。
     * @param readTimeout   超时时间，单位毫秒。
     * @return 响应字符串。
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> headValues, Map<String, String> params, String charset, int readTimeout)
            throws IOException {
        HttpURLConnection conn = null;

        try {
            String ctype = "application/x-www-form-urlencoded;charset=" + charset;
            String query = buildQuery(params, charset);
            conn = getConnection(buildGetUrl(url, query), METHOD_GET, ctype);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(readTimeout);

            if (!CollectionUtils.isEmpty(headValues)) {
                for (Map.Entry<String, String> entry : headValues.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            return getResponseAsString(conn);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 执行HTTP POST请求。
     *
     * @param url         请求地址。
     * @param paramValues 请求参数。
     * @return 响应字符串。
     * @throws IOException
     */
    public static HttpResult doPost(String url, Map<String, String> paramValues, int readTimeout) throws IOException {
        return doPost(url, null, paramValues, DEFAULT_CHARSET, readTimeout);
    }

    /**
     * 执行HTTP POST请求。
     *
     * @param url         请求地址。
     * @param headValues  头属性参数集合。
     * @param paramValues 请求参数。
     * @return 响应字符串。
     * @throws IOException
     */
    public static HttpResult doPost(String url, Map<String, String> headValues, Map<String, String> paramValues, int readTimeout) throws IOException {
        return doPost(url, headValues, paramValues, DEFAULT_CHARSET, readTimeout);
    }

    /**
     * 执行HTTP POST请求。
     *
     * @param url         请求地址。
     * @param headValues  头属性参数集合。
     * @param paramValues 请求参数。
     * @param charset     字符集，如UTF-8, GBK, GB2312。
     * @return 响应字符串。
     * @throws IOException
     */
    public static HttpResult doPost(String url, Map<String, String> headValues, Map<String, String> paramValues, String charset, int readTimeout) throws IOException {
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(paramValues, charset);
        byte[] content = {};
        if (query != null) {
            content = query.getBytes(charset);
        }
        return doPost(url, headValues, ctype, content, readTimeout, charset);
    }

    /**
     * 执行HTTP POST请求。
     *
     * @param url         请求地址。
     * @param headValues  头属性参数集合。
     * @param contentType 请求类型。
     * @param content     请求字节数组。
     * @return 响应字符串。
     * @throws IOException
     */
    public static HttpResult doPost(String url, Map<String, String> headValues, String contentType, byte[] content, int readTimeout, String charset) throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        try {
            conn = getConnection(new URL(url), METHOD_POST, contentType);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(readTimeout);

            if (!CollectionUtils.isEmpty(headValues)) {
                for (Map.Entry<String, String> entry : headValues.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            out = conn.getOutputStream();
            out.write(content);
            Map<String, List<String>> headers = conn.getHeaderFields();
            return new HttpResult(conn.getResponseCode(), getResponseAsString(conn, charset),headers);
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 执行HTTP POST请求。
     *
     * @param url         请求地址。
     * @param headValues  请求头参数。
     * @param content     请求字节数组。
     * @param charset     字符编码。
     * @param readTimeout 读取超时时间(毫秒)。
     * @return 响应字符串。
     * @throws IOException
     */
    public static HttpResult doPost(String url, String charset, byte[] content, Map<String, String> headValues, int readTimeout) throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        try {
            conn = getConnection(new URL(url), METHOD_POST, headValues);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(readTimeout);

            out = conn.getOutputStream();
            out.write(content);

            Map<String, List<String>> headers = conn.getHeaderFields();

            return new HttpResult(conn.getResponseCode(), getResponseAsString(conn, charset),headers);
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 辅助类，封装HTTP返回结果，包含状态码和内容。
     */
    public static class HttpResult {
        /*状态码*/final public int code;
        /*如果失败，错误信息；如果成功，响应内容*/final public String content;
        final public Map<String, List<String>> headers;

        /**
         * @param code    状态码。
         * @param content 如果失败，错误信息；如果成功，响应内容。
         */
        public HttpResult(int code, String content, Map<String, List<String>> headers) {
            this.code = code;
            this.content = content;
            this.headers = headers;
        }

        /**
         * {@link Object#toString()}。
         */
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }
}
