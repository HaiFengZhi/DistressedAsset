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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Cookie辅助方法类集合。
 *
 * @author Yelin.G at 2015/07/22
 */
public final class CookieUtils {

    private CookieUtils() {
        super();
    }

    /**
     * 请求中根据Cookie名称获取{@link Cookie}。
     *
     * @param request {@link HttpServletRequest}。
     * @param cookieName Cookie名称。
     * @return {@link Cookie}。
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        if (cookieName == null || cookieName.isEmpty()) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * 解码Cookie，通过指定的编码。
     *
     * @param cookie {@link Cookie}。
     * @param encoding 编码。
     * @return Cookie解码后的字符串。
     * @throws UnsupportedEncodingException
     */
    public static String decodeCookie(Cookie cookie, String encoding) throws UnsupportedEncodingException {
        if (cookie == null || cookie.getValue() == null) {
            return null;
        }
        return URLDecoder.decode(cookie.getValue(), encoding);
    }

    /**
     * 请求中根据Cookie名称获取{@link Cookie}值，并解码成字符串。
     *
     * @param request {@link HttpServletRequest}。
     * @param cookieName Cookie名称。
     * @param encoding 编码。
     * @return 解码后的字符串。
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encoding) throws UnsupportedEncodingException {
        return decodeCookie(getCookie(request, cookieName), encoding);
    }

    /**
     * 通过域名和Cookie名删除Cookie。
     *
     * @param response {@link HttpServletResponse}。
     * @param domain 域名。
     * @param cookieName Cookie名称。
     */
    public static void deleteCookie(HttpServletResponse response, String domain, String cookieName) {
        if (cookieName == null || cookieName.isEmpty()) {
            return;
        }
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    /**
     * 通过域名，Cookie名称，值，过期时间，编码创建Cookie。
     *
     * @param response {@link HttpServletResponse}。
     * @param domain 域名。
     * @param cookieName Cookie名称。
     * @param value 值。
     * @param maxAge 过期时间。
     * @param encoding 编码。
     * @throws UnsupportedEncodingException
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String domain, String cookieName, String value, Integer maxAge, String encoding) throws UnsupportedEncodingException {
        Cookie cookie = new Cookie(cookieName, URLEncoder.encode(value, encoding));
        cookie.setPath("/");
        cookie.setDomain(domain);

        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }
}
