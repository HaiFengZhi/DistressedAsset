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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

/**
 * GUID生成方法类集合。
 *
 * @author Yelin.G at 2015/08/01 7:24
 */
public final class GUIDUtils {
    /**
     * sql server 无意义guid值
     */
    public final static String MEANINGLESS_GUID = "00000000-0000-0000-0000-000000000000";
    private final static Random RANDOM = new Random();

    private GUIDUtils() {
        super();
    }

    /**
     * 通过用户ID和对应的枚举类型整型值生成一个GUID。
     *
     * <p>
     *     在老平台中，很多地方的主键都是通过这种算法生成唯一的
     *     UUID。
     * </p>
     *
     * @param guidCategory guid类型(枚举值)码 。
     * @param userID       用户ID。
     * @return GUID。
     */
    public static String makeGUIDByCategoryAndUser(int guidCategory, Long userID) {
        if (userID == null) {
            userID = RANDOM.nextLong();
        }
        //由15位当前时间+8位创建的编号+2位编号的类型+7位随机编号
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        Random random = new Random();
        StringBuilder uuid = new StringBuilder();
        uuid.append(formatter.format(calendar.getTime()))
                .append(9)
                .append(String.format("%06d", (Math.abs(userID) + calendar.get(Calendar.DAY_OF_YEAR) + calendar.get(Calendar.MILLISECOND))))
                .append(String.format("%03d", Math.abs(guidCategory)))
                .append(String.format("%07d", random.nextInt(9999999)));

        return makeUUIDByAdditionOfHyphen(uuid.toString());
    }

    /**
     * 生成当前时间的十七位数加四位随机数的字符串。
     *
     * <p>
     *     常用于生成文件名。
     * </p>
     *
     * @return 21位字符串。
     */
    public static String makeUploadFileName(){
        Random random = new Random();
        //由17位当前时间（YYYYMMddHHmmssSSS）+4位随机数组成文件名
        String uuid = MyDateUtils.formatDate(MyDateUtils.now(), "yyyyMMddHHmmssSSS") +
                String.format("%04d", random.nextInt(9999));
        return uuid;
    }

    /**
     * 生成去掉-的UUID；
     *
     * @return uuid字符串。
     */
    public static String makeUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static void main(String[] args) {

        System.out.println(GUIDUtils.makeUploadFileName());
        System.out.println(GUIDUtils.makeUUID());
        System.out.println(System.getProperty("user.dir"));
    }

    /**
     * 去掉UUID中的'-'字符。
     *
     * @return 32位的UUID。
     */
    public static String makeUUIDByRemovalOfHyphen(String nativeUUID) {
        return nativeUUID.replaceAll("-", "");
    }

    /**
     * 去掉横杠UUID中，增加'-'字符。
     *
     * @param removalUUID 无横杠的uuid。
     * @return 36为uuid。
     */
    public static String makeUUIDByAdditionOfHyphen(String removalUUID) {
        return new StringBuilder(removalUUID.substring(0, 8)).append("-")
                .append(removalUUID.substring(8, 12)).append("-")
                .append(removalUUID.substring(12, 16)).append("-")
                .append(removalUUID.substring(16, 20)).append("-")
                .append(removalUUID.substring(20, 32)).toString();
    }

    public enum GUIDCategory {

        // 上传文件编号
        UPLOADFILE(1),
        //系统公告
        BULLETIN(2),
        INFORMATION(3)
        ;

        private int type;

        GUIDCategory(int index) {
            this.type = index;
        }

        /**
         * {@link #type}的getter方法。
         */
        public int getType() {
            return type;
        }
    }
}