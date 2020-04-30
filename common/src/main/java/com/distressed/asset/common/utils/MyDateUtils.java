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

import com.distressed.asset.common.exception.ParameterException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期，时间相关操作类。
 *
 * @author fish at 2015/7/17 15:26:17
 */
public final class MyDateUtils extends DateUtils {
    public static final long MILLISECONDS_OF_ONE_DAY = 24 * 3600 * 1000;

    public static final String CLASSIC_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDSQL = "yyyy-MM-dd";
    public static final String YMDSQL2 = "yyyy/MM/dd";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM","yyyyMMdd"};

    private MyDateUtils() {
        super();
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }


    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(String dateStr, Object... pattern) {
        Date date = parseDate(dateStr);
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }


    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 判断当前时间是否在指定的开始时间和结束时间范围之内。
     *
     * @param start 开始时间。
     * @param end   结束时间。
     * @return true：是；false：否。
     */
    public static boolean isNowRangeIn(Date start, Date end) {
        Calendar nowCalendar = Calendar.getInstance();

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);

        return nowCalendar.after(startCalendar) && nowCalendar.before(endCalendar);
    }

    /**
     * 取得指定时间增加相应年份数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     *
     * @param date  日期，字符串。
     * @param years 年份数。
     * @return 增加年份数后的时间。
     */
    public static String addYears(String date, int years) {
        Calendar calendar = getCalendar(parseString(date));
        calendar.add(Calendar.YEAR, years);
        return formatByClassic(calendar.getTime());
    }

    /**
     * 取得指定时间增加相应年份数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     *
     * @param date  日期。
     * @param years 年份数。
     * @return 增加年份数后的时间。
     */
    public static String addYears2(Date date, int years) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.YEAR, years);
        return formatByClassic(calendar.getTime());
    }

    /**
     * 取得指定时间增加相应年份数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     *
     * @param date  日期。
     * @param years 年份数。
     * @return 增加年份数后的时间。
     */
    public static String addYears(Timestamp date, int years) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.YEAR, years);
        return formatByClassic(calendar.getTime());
    }

    /**
     * 取得指定时间增加相应月份数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     * @param date  日期，字符串。
     * @param months 月份数。
     * @return 增加月份数后的时间。
     */
    public static String addMonths(String date, int months) {
        Calendar calendar = getCalendar(parseString(date));
        calendar.add(Calendar.MONTH, months);
        return formatByClassic(calendar.getTime());
    }

    /**
     * 取得指定时间增加相应月份数后的时间。
     *
     * @param time    日期。
     * @param months  月份数。
     * @return 增加月份数后的时间。
     */
    public static Timestamp addMonths(Timestamp time, int months) {
        Calendar calendar = getCalendar(time);
        calendar.add(Calendar.MONTH, months);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 取得指定时间增加相应月份数后的时间。
     *
     * @param time    日期。
     * @param months  月份数。
     * @return 增加月份数后的时间。
     */
    public static Date addMonths(Date time, int months) {
        Calendar calendar = getCalendar(time);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * 取得指定时间增加相应天数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     * @param time 时间。
     * @param days 增加的天数。
     * @return 增加天数后的时间。
     */
    public static Timestamp addDays(Timestamp time, int days) {
        long now = time == null ? System.currentTimeMillis() : time.getTime();
        return new Timestamp(now + MILLISECONDS_OF_ONE_DAY * days);
    }

    /**
     * 取得指定时间增加相应天数后的日期。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     * @param date 日期。
     * @param days 增加的天数。
     * @return 增加天数后的日期。
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    /**
     * 取得指定时间增加相应天数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     * @param date  日期，字符串。
     * @param days 天数。
     * @return 增加天数后的时间。
     */
    public static String addDays(String date, int days) {
        Calendar calendar = getCalendar(parseString(date));
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return formatByClassic(calendar.getTime());
    }

    /**
     * 取得指定时间增加相应小时数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     * @param date  日期，字符串。
     * @param hours 小时数。
     * @return 增加小时数后的时间。
     */
    public static String addHours(String date, int hours) {
        Calendar calendar = getCalendar(parseString(date));
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return formatByClassic(calendar.getTime());
    }

    /**
     * 取得指定时间增加相应小时数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     * @param date  日期。
     * @param hours 小时数。
     * @return 增加小时数后的时间。
     */
    public static Date addHours(Timestamp date, int hours) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    /**
     * 取得指定时间增加相应小时数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     * @param date  日期。
     * @param hours 小时数。
     * @return 增加小时数后的时间。
     */
    public static Timestamp addHours(Date date, int hours){
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * 取得指定时间增加相应分钟数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     * @param date  日期。
     * @param minutes 分钟数。
     * @return 增加分钟数后的时间。
     */
    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    /**
     * 取得指定时间增加相应秒数后的时间。
     *
     * <p>
     *     基于多种比较常用的日期格式。
     * </p>
     *
     * @param date  日期，字符串。
     * @param seconds 秒数。
     * @return 增加秒数后的时间。
     */
    public static String addSecond(String date, int seconds) {
        Calendar calendar = getCalendar(parseString(date));
        calendar.add(Calendar.SECOND, seconds);
        return formatByClassic(calendar.getTime());
    }

    /**
     * 取得当前系统时间。
     *
     * @return 取得当前系统时间。
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 计算两个日期之间相差的天数。
     *
     * @param lower 时间下限。
     * @param upper 时间上限。
     * @return 相差天数。
     */
    public static int getIntervalByDays(Date lower, Date upper) {
        if (lower == null || upper == null) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            lower = sdf.parse(sdf.format(lower));
            upper = sdf.parse(sdf.format(upper));
        } catch (ParseException ex) {
            throw new IllegalStateException(ex);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(lower);
        long time1 = cal.getTimeInMillis();
        cal.setTime(upper);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的天数。
     *
     * @param lower 时间下限。
     * @param upper 时间上限。
     * @return 相差天数。
     */
    public static int getIntervalByDays(Timestamp lower, Timestamp upper) {
        if (lower == null || upper == null) return 0;
        return getIntervalByDays((Date) lower, (Date) upper);
    }

    /**
     * 计算两个时间相差多少秒。
     *
     * @param str1 时间。
     * @param str2 被减时间。
     * @return 相差秒。
     */
    public static long getIntervalBySeconds(String str1, String str2) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long diff = 0;
        one = df.parse(str1);
        two = df.parse(str2);
        long time1 = one.getTime();
        long time2 = two.getTime();

        diff = time1 - time2;
        return diff / 1000;
    }

    /**
     * 判断指定时间<code>date</code>在<code>timeoutAtMinutes</code>分钟
     * 之后是否小于当前时间，也就是说截止到调用方法为止，之前的时间是否已经超过
     * 了指定的分钟。
     *
     * @param date             比较时间。
     * @param timeoutAtMinutes 指定超时时间 单位：分钟。
     * @return true：没有超过；false：超过了。
     */
    public static boolean isOverTime(Date date, int timeoutAtMinutes) {
        Date $date = date;
        $date = MyDateUtils.addMinutes($date, timeoutAtMinutes);
        return $date.before(new Date());
    }

    /**
     * 将字符串日期转换成{@link Date}。
     *
     * @param date 字符串日期。
     * @param format 字符串日期格式。
     * @return {@link Date}。
     */
    public static Date parseString(String date, String format) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        String formatStr;
        if (format == null) {
            formatStr = CLASSIC_DATE_FORMAT;
        } else {
            formatStr = format;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        try {
            return sdf.parse(date);
        } catch (ParseException ex) {
            throw new ParameterException(ex);
        }
    }

    /**
     * 将字符串日期转换成{@link Date}。
     *
     * @param date 字符串日期。
     * @return {@link Date}。
     */
    public static Date parseString(String date) {
        if (CommonUtils.isBlank(date)) {
            return null;
        }
        String ydate = "";
        String ytime = "";
        String dType = "yyyy年MM月dd日";
        String tType = "HH:mm:ss";
        if (3 < date.split(" ").length) {
            try {
                return new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.SIMPLIFIED_CHINESE).parse(date);
            } catch (ParseException e) {
                return null;
            }
        }

        if (date.indexOf(" ") > 0) {
            ydate = date.substring(0, date.indexOf(" "));
        } else {
            ydate = date;
        }
        if (10 < date.indexOf(" ") + 1) {
            ytime =
                    date.substring(date.indexOf(" ") + 1, date.length());
        } else {
            ytime = "00:00:00";
        }

        if (ydate.indexOf("-") > 0) {
            dType = "yyyy-MM-dd";
        } else if (ydate.indexOf("年") > 0) {
            if ((ydate.indexOf("年") > 0) && (ydate.indexOf("月") > 0) &&
                    (ydate.indexOf("年") < ydate.indexOf("月"))) {
                dType = "yyyy年MM月dd日";
            } else if ((ydate.indexOf("年") > 0) &&
                    (ydate.indexOf("月") > 0) &&
                    (ydate.indexOf("年") > ydate.indexOf("月"))) {
                dType = "MM月dd日yyyy年";
            }
        } else if ((ydate.indexOf("/") > 0) && (ydate.indexOf("/") == 4)) {
            dType = "yyyy/MM/dd";
        } else if ((ydate.indexOf("/") > 0) &&
                (ydate.indexOf("/") == 2)) {
            dType = "MM/dd/yyyy";
        }

        if (1 < ytime.indexOf("时")) {
            tType = "HH时mm分ss秒";
            if (ytime.length() == 6) {
                ytime = ytime + "00秒 ";
            }
        } else if (1 < ytime.indexOf(":")) {
            tType = "HH:mm:ss";
            if (ytime.length() == 5) {
                ytime = ytime + ":00";
            }
        } else {
            tType = null;
        }
        date = ydate + " " + ytime;
        try {
            return new SimpleDateFormat(dType + " " + tType).parse(date);
        } catch (ParseException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * 将字符串日期转换成{@link Timestamp}。
     *
     * @param date 字符串日期，注：String的类型必须形如： yyyy-mm-dd hh:mm:ss[.f...] 这样的格式，中括号表示可选，否则报错！！！
     * @return {@link Timestamp}。
     */
    public static Timestamp parseTimestamp(String date) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (!StringUtils.isEmpty(date)) {
            try {
                timestamp = Timestamp.valueOf(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return timestamp;
    }

    /**
     * 辅助方法：格式化日期，使用最常用的日期格式：yyyy-MM-dd hh:mm:ss。
     *
     * @return 格式化后的日期字符串。
     */
    public static String formatByClassic(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 辅助方法：格式化日期，使用最常用的日期格式：yyyy-MM-dd hh:mm。
     *
     * @return 格式化后的日期字符串。
     */
    public static String formatToMinute(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    /**
     * 辅助方法：获取指定{@link Date}的{@link Calendar}对象。
     *
     * @param date 日期，{@link Date}。
     * @return 日期，{@link Calendar}。
     */
    private static Calendar getCalendar(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 判断指定{@link Date}的{@link Calendar}对象是上午还是下午。
     *
     * @return “0”是上午 ，“1”是下午。
     */
    public static int getGregorianCalendarAMPM() {
        GregorianCalendar ca = new GregorianCalendar();
        return ca.get(GregorianCalendar.AM_PM);
    }

    /**
     * 判断<code>first</code>是否<=<code>second</code>。
     *
     * <p>
     *     比较日期大小，精确到天，其余时间忽略。
     * </p>
     *
     * @param first  开始时间。
     * @param second 结束时间。
     * @return true：first < second；false：first >= second。
     */
    public static boolean isLessThanByDay(Date first, Date second) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            first = sdf.parse(sdf.format(first));
            second = sdf.parse(sdf.format(second));
        } catch (ParseException ex) {
            throw new IllegalStateException(ex);
        }

        long firstDays = first.getTime()/(1000 * 3600 * 24L);

        long secondDays = second.getTime()/(1000 * 3600 * 24L);

        long days = firstDays - secondDays;

        return days < 0;

    }

    /**
     * 判断<code>first</code>是否>=<code>second</code>。
     *
     * <p>
     *     比较日期大小，精确到天，其余时间忽略。
     * </p>
     *
     * @param first  开始时间。
     * @param second 结束时间。
     * @return true：first > second；false：first<= second。
     */
    public static boolean isGreaterThanByDay(Date first, Date second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            first = sdf.parse(sdf.format(first));
            second = sdf.parse(sdf.format(second));
        } catch (ParseException ex) {
            throw new IllegalStateException(ex);
        }

        long firstDays = first.getTime()/(1000 * 3600 * 24L);

        long secondDays = second.getTime()/(1000 * 3600 * 24L);

        long days = firstDays - secondDays;
        return days > 0;

    }

    /**
     * 判断<code>first</code>是否==<code>second</code>。
     *
     * <p>
     *     比较日期大小，精确到天，其余时间忽略。
     * </p>
     *
     * @param first  开始时间。
     * @param second 结束时间。
     * @return true：first = second；false：first <> second。
     */
    public static boolean isEqualByDay(Date first, Date second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            first = sdf.parse(sdf.format(first));
            second = sdf.parse(sdf.format(second));
        } catch (ParseException ex) {
            throw new IllegalStateException(ex);
        }

        long firstDays = first.getTime()/(1000 * 3600 * 24L);

        long secondDays = second.getTime()/(1000 * 3600 * 24L);

        long days = firstDays - secondDays;
        return days == 0;
    }

    /**
     * 判断<code>first</code>是否<=<code>second</code>。
     *
     * <p>
     *     比较时间大小，精确到毫秒。
     * </p>
     *
     * @param first  开始时间。
     * @param second 结束时间。
     * @return true：first < second；false：first >= second。
     */
    public static boolean isLessThanByTime(Date first, Date second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            first = sdf.parse(sdf.format(first));
            second = sdf.parse(sdf.format(second));
        } catch (ParseException ex) {
            throw new IllegalStateException(ex);
        }

        long firstDays = first.getTime();

        long secondDays = second.getTime();

        long days = firstDays - secondDays;

        return days < 0;

    }

    /**
     * 计算两个时间相差距离多少天多少小时多少分多少秒。
     *
     * @param one 开始时间。
     * @param two 结束时间。
     * @return "xx天xx小时xx分xx秒"格式的字符串。
     */
    public static String getDistanceTimeString(Date one, Date two) {
        if (one == null || two == null) {
            return null;
        }

        long time1 = one.getTime();
        long time2 = two.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        long day = diff / (24 * 60 * 60 * 1000);
        long hour = (diff / (60 * 60 * 1000) - day * 24);
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        return day + "天" + hour + "时" + min + "分" + sec + "秒";
    }

    /**
     * 计算两个时间相差距离多少天多少小时多少分多少秒。
     *
     * @param one 开始时间。
     * @param two 结束时间。
     * @return "xx天xx小时xx分xx秒"格式的字符串。
     */
    public static String getDistanceTimeString2(Date one, Date two) {
        if (one == null || two == null) {
            return null;
        }

        long time1 = one.getTime();
        long time2 = two.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        long day = diff / (24 * 60 * 60 * 1000);
        long hour = (diff / (60 * 60 * 1000) - day * 24);
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        return (day > 0 ? (day + "天") : "") + hour + "时" + min + "分";
    }

    /**
     * 计算两个日期直接相差的月份数。
     *
     * @param lower 时间下限。
     * @param upper 时间上限。
     * @return 相差月份数。
     */
    public static int getIntervalByMonths(Date lower, Date upper) {
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        beginCalendar.setTime(lower);
        endCalendar.setTime(upper);
        int difMonth = (endCalendar.get(Calendar.YEAR) - beginCalendar.get(Calendar.YEAR)) * 12 + endCalendar.get(Calendar.MONTH) - beginCalendar.get(Calendar.MONTH);
        int difDay = endCalendar.get(Calendar.DATE) - beginCalendar.get(Calendar.DATE);
        if (difDay < 0) {
            difMonth = difMonth - 1;
        }
        return difMonth;

    }
    /**
     * 格式化当前时间(指定时分)
     * @param  date 时间
     * @param hour 时。
     * @param minutes 分
     * @return 通过时分获取当前时间
     */
    public static Date getDateBySetTime(Date date, Integer hour, Integer minutes){
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minutes);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTime();
    }

    /**
     * 格式化当前时间(指定到第二天0点0分0秒)
     * @param  date 时间
     * @return 通过时分获取当前时间
     */
    public static Date getNextDate(Date date){
        Calendar calendar = GregorianCalendar.getInstance();
        date=addDays(date,1);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTime();
    }

    /**
     * 获取当前时间的小时数
     * @param  date 时间
     * @return 通过时分获取当前时间
     */
    public static int getHour(Date date){
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前时间的分钟数
     * @param  date 时间
     * @return 通过时分获取当前时间分钟数
     */
    public static int getMinute(Date date){
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }


    /**
     * 根据月份获取对应的季度数。
     *
     * @param month 月份数。
     * @return 相差月份数。
     */
    public static int getQuarterByMonth(int month) {
        if (month >= 1 && month <= 3) {
            return 1;
        } else if (month >= 4 && month <= 6) {
            return 2;
        } else if (month >= 7 && month <= 9) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * 根据时间取当前季度的第一天。
     * @param date 输入时间。
     * @return 根据时间取当前季度的第一天。
     */
    public static Date getFirstDayOfQuarter(Date date)   {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        int curMonth = cDay.get(Calendar.MONTH);
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH){
            cDay.set(Calendar.MONTH, Calendar.JANUARY);
        }
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE){
            cDay.set(Calendar.MONTH, Calendar.APRIL);
        }
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
            cDay.set(Calendar.MONTH, Calendar.JULY);
        }
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
            cDay.set(Calendar.MONTH, Calendar.OCTOBER);
        }
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cDay.getTime();
    }

    /**
     * 如果要用，放在自己的类里边，这个应该不是通用类的方法。
     *
     * 匹配时间是否在指定范围内, -1 为大于当前时间，0 为不再间隔时间内。
     *
     * @param time 时间。
     * @param interval 间隔时间 以秒为单位。
     * @return
     **/
    public static int checkTimeWithinInterval(String time, int interval) {
        Date date = parseString(time, "yyyyMMddHHmmss");
        if (date.getTime() > System.currentTimeMillis()) {
            return -1;
        }
        if (date.getTime() + interval * 1000 > System.currentTimeMillis()) {
            return 1;
        }
        return 0;
    }


    /**
     * 将日期转换为字符串。
     * @param date
     * @return
     */
    public static String convertDate(Date date){
        if(date==null){
            return  "-";
        }else{
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
    }

    /**
     * 将日期转换为字符串。
     * @param date
     * @return
     */
    public static String convertFileDate(Date date){
        if(date==null){
            return  "-";
        }else{
            return new SimpleDateFormat("yyyyMMdd").format(date);
        }
    }

    /**
     *  将时间戳转换为字符串
     * @param timestamp
     * @return
     */
    public static String covertTime(Timestamp timestamp){
        if(timestamp==null){
            return  "--";
        }else{
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
        }
    }

    /**
     * 将日期转换为字符串。
     * @param date
     * @return
     */
    public static String convertDate(Date date, String format){
        if(date==null){
            return  "-";
        }else{
            return new SimpleDateFormat(format).format(date);
        }
    }

    /**
     * 格式化日期，去除时分秒
     * @param date
     * @return
     */
    public static Date truncDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        try {
            date = format.parse(format.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当月的 天数
     */
    public static int getCurrentMonthDay(Calendar calendar) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }

    public static String formatByClassicNoSpace(Date date) {
        return (new SimpleDateFormat("yyyy-MM-ddHH:mm:ss")).format(date);
    }

    /**
     * 获取当前星期几。第一天是星期天。
     * @param calendar
     * @return
     */
    public static int getDayOfWeek (Calendar calendar) {
        //若一周第一天为星期天，则-1
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if(calendar.getFirstDayOfWeek() == Calendar.SUNDAY){
            weekDay = weekDay - 1;
            if(weekDay == 0){
                weekDay = 7;
            }
        }

        return weekDay;
    }

    /**
     * 获取平台运营年日，以#分隔。
     *
     * @return 年#日。
     */
    public static String getOperationKeepTime() {
        String dateStr = "-03-28 00:00:00";
        Calendar start = Calendar.getInstance();
        start.setTime(Timestamp.valueOf("2009" + dateStr));
        Calendar now = Calendar.getInstance();
        if (new Timestamp(now.getTimeInMillis()).after(Timestamp.valueOf(now.get(Calendar.YEAR) + dateStr))) {
            return (now.get(Calendar.YEAR) - start.get(Calendar.YEAR)) +
                    "#" +
                    MyDateUtils.getIntervalByDays(Timestamp.valueOf(now.get(Calendar.YEAR) + dateStr), now.getTime());
        } else {
            return (now.get(Calendar.YEAR) - start.get(Calendar.YEAR) - 1) +
                    "#" +
                    MyDateUtils.getIntervalByDays(Timestamp.valueOf((now.get(Calendar.YEAR)-1) + dateStr), now.getTime());
        }
    }
    /**
     * 获取当前时间 yyyyMMddHHmmss
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }
    /**
     * 时间转换成字符串
     * @param date 时间
     * @param formatType 格式化类型
     * @return String
     */
    public static String date2String(Date date, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(date);
    }

    /**
     * 某月的最后一天
     */
    public static int getMonDays(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int MaxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return MaxDay;
    }

    /**
     * 某月第一天
     *
     * @param d Date型-日期
     * @return
     */
    public static String getFirstDayOFMon(Date d) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(d);
        cd.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cd.getTime());
    }

    /**
     * 某月第一天
     *
     * @param date String型-日期
     * @return
     */
    public static String getFirstDayOFMon(String date) {
        return getFirstDayOFMon(parseDate(date));
    }

    /**
     * 某月最后一天
     *
     * @param d Date型-日期
     * @return
     */
    public static String getLastDayOFMon(Date d) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(d);
        cd.set(Calendar.DAY_OF_MONTH, cd.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cd.getTime());
    }

    /**
     * 某月最后一天
     *
     * @param date String型-日期
     * @return
     */
    public static String getLastDayOFMon(String date) {
        return getLastDayOFMon(parseDate(date));
    }
}
