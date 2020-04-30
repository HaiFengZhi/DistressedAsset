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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 金额相关计算，集合操作类。
 *
 * @author fish at 2015/11/23 10:10
 */
public final class MoneyUtils {

    /**
     * 金额0.00。
     */
    public static final BigDecimal ZERO = BigDecimal.valueOf(0.00);

    /**
     * 金额100.00。
     */
    public static final BigDecimal HUNDRED = new BigDecimal(100.00);

    /**
     * 金额1.00。
     */
    public static final BigDecimal ADDONE = BigDecimal.valueOf(1.00);

    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖" };
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
            "佰", "仟" };
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

    private MoneyUtils() {
        super();
    }

    /**
     * 四舍五入保留1位小数（四舍六入五成双）。
     *
     * @param amount 金额。
     * @return  四舍五入保留1位小数后金额。
     */
    public static BigDecimal decimal1ByUp(BigDecimal amount) {
        return amount.setScale(1, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 四舍五入保留2位小数（四舍六入五成双）。
     *
     * @param amount 金额。
     * @return  四舍五入保留2位小数后金额。
     */
    public static BigDecimal decimal2ByUp(BigDecimal amount) {
        return amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 四舍五入保留4位小数（四舍六入五成双）。
     *
     * @param amount 金额。
     * @return 四舍五入保留4位小数后金额。
     */
    public static BigDecimal decimal4ByUp(BigDecimal amount) {
        return amount.setScale(4, BigDecimal.ROUND_HALF_EVEN);
    }


    /**
     * 除法，四舍五入保留2位小数。
     *
     * @param divideAmount 金额。
     *  @param dividedAmount 被除的金额。
     * @return  四舍五入保留2位小数后金额。
     */
    public static BigDecimal divide2ByUp(BigDecimal divideAmount, BigDecimal dividedAmount) {
        return divideAmount.divide(dividedAmount,2, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 除法，四舍五入保留4位小数。
     *
     * @param divideAmount 金额。
     *  @param dividedAmount 被除的金额。
     * @return  四舍五入保留2位小数后金额。
     */
    public static BigDecimal divide4ByUp(BigDecimal divideAmount, BigDecimal dividedAmount) {
        return divideAmount.divide(dividedAmount,4, BigDecimal.ROUND_HALF_EVEN);
    }


    /**
     * 是否大于0。
     *
     * @param amount 金额。
     * @return true：大于0；false；小于等于0。
     */
    public static boolean isGreaterThanZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 1;
    }

    /**
     * 是否小于0。
     *
     * @param amount 金额。
     * @return true：小于0；false：大于等于0。
     */
    public static boolean isLessThanZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == -1;
    }

    /**
     * 是否等于0。
     *
     * @param amount 金额。
     * @return true：等于0；false：大于小于0。
     */
    public static boolean isEqualZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }


    /**
     * 第一个是否大于第二个。
     *
     * @param first  金额。
     * @param second 金额。
     * @return true：first>second；false：first<=second。
     */
    public static boolean isGreaterThan(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == 1;
    }

    /**
     * 第一个是否小于第二个。
     *
     * @param first  金额。
     * @param second 金额。
     * @return true：first<second；false：first >=second。
     */
    public static boolean isLessThan(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == -1;
    }

    /**
     * 第一个是否等于第二个。
     *
     * @param first  金额。
     * @param second 金额。
     * @return true：first=second；false：first ><second。
     */
    public static boolean isEqual(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == 0;
    }

    public static  BigDecimal POINT_BILLION = new BigDecimal("100000000");
    public static  BigDecimal TEN_THOUSANDS = new BigDecimal("10000");

    /**
     * 金额格式化为,###.00的金额,即保留两位小数。
     *
     * @param amount 需要显示成字符传,###.###的金额。
     * @return  返回String，格式为,###.###。
     */
    public static String formatAmountAsString(BigDecimal amount) {
        return amount != null ? new DecimalFormat(",##0.00").format(amount) : "0.00";
    }

    /**
     * 金额格式化为,###的金额,即保留到整数。
     *
     * @param amount 需要显示成字符传,###的金额。
     * @return  返回String，格式为,###。
     */
    public static String formatAmountAsString2(BigDecimal amount) {
        return amount != null ? new DecimalFormat(",##0").format(amount) : "0";
    }

    /**
     * 金额格式化为去除小数点后的0
     *
     * @param amount 需要显示成字符传,###的金额。
     * @return  返回String，格式为,###。
     */
    public static String formatAmountAsString3(BigDecimal amount){

        return  amount != null ? NumberFormat.getInstance().format(amount):"0";
    }

    /**
     * 利率格式化为,###.00的金额,即保留两位小数。
     *
     * @param rate 需要显示成字符传,###.###的金额。
     * @return  返回String，格式为,###.###。
     */
    public static String rateAsString(BigDecimal rate) {
        if (rate != null) {
            if (rate.compareTo(rate.setScale(0, BigDecimal.ROUND_DOWN)) != 0) {
                return rate.stripTrailingZeros().toString();
            } else {
                return new DecimalFormat(",##0.00").format(rate);
            }
        }

        return "0.00";
    }

    /**
     * 将格式化的字符串金额反转为{@link BigDecimal}。
     *
     * @param amount 格式为,###.###。
     * @return {@link BigDecimal}。
     */
    public static BigDecimal parseAmount(String amount) {
        return new BigDecimal(amount.replaceAll(",", ""));
    }


    public static  BigDecimal pointBillion= new BigDecimal("100000000");
    public static  BigDecimal tenThousands= new BigDecimal("10000");
    /**
     * 将普通格式的金额或者数字转换为带亿万的字符串。
     * @Param nature 必须是数字哟。
     * @Param withpetty 是否包含小于万元的数据。
     * return 返回带亿万等单位的字符串。
     **/
    public static  String formateNumberBillion(String nature,Boolean withpetty) throws Exception{
        if(StringUtils.isEmpty(nature)){
            throw new Exception("nature is wrong parameter,check your data");
        }
        BigDecimal temp = new BigDecimal(nature);
        //整除1亿
        String output= "";
        if(temp.compareTo(pointBillion)>0) {
            BigDecimal pointbillionUnit = temp.divide(pointBillion);
            if (pointbillionUnit.compareTo(BigDecimal.ONE) > 0) {
                output = pointbillionUnit.intValue() + "</span>亿<span class=\"big\">";
                temp = temp.subtract(pointBillion.multiply(new BigDecimal(pointbillionUnit.intValue())));
            }
        }
        //整除1万
        if(temp.compareTo(tenThousands)>0){
            BigDecimal  tenThousandsUnit= temp.divide(tenThousands);
            if(tenThousandsUnit.compareTo(BigDecimal.ONE)>0){
                output =output +  tenThousandsUnit.intValue() + "</span>万<span>";
                temp  = temp.subtract(tenThousands.multiply(new BigDecimal(tenThousandsUnit.intValue())));
            }
        }
        if(withpetty){
            output=  output + "<span class=\"big\">" +temp.intValue() + "</span>";
        }
        return output ;
    }

    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param numberOfMoney
     *            输入的金额
     * @return 对应的汉语大写
     */
    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }

    /**
     * 格式化金额为字符串。
     *
     * @param number 金额。
     * @param isNumber 金额还是个数？
     * @return 数字转换好后的中文金额。
     */
    public static String formatNumberAsChineseWithHTML(BigDecimal number,boolean isNumber){

        BigInteger account = number.toBigInteger();
        Double s = null;
        StringBuffer showMoney = new StringBuffer();

        if (isNumber){
            if(account.compareTo(new BigInteger("10000"))==1 || account.compareTo(new BigInteger("10000"))==0){
                BigInteger b1 = account.remainder(new BigInteger("100000000")).divide(new BigInteger("10000"));
                s = Math.floor(new Double(b1.toString()));
                showMoney.append(s.intValue()+"<b>万</b>");
            }
            if(account.compareTo(new BigInteger("1000"))==1 || account.compareTo(new BigInteger("1000"))==0){
                BigInteger b1 = account.remainder(new BigInteger("10000")).divide(new BigInteger("1"));
                s = Math.floor(new Double(b1.toString()));
                if(s.intValue()>0){
                    showMoney.append(s.intValue()+"<b>个</b>");
                }
            }

        }else{

            if(account.compareTo(new BigInteger("10000"))==-1) showMoney.append("0.00");

            if(account.compareTo(new BigInteger("100000000"))==1 || account.compareTo(new BigInteger("100000000"))==0){
                BigInteger b1 = account.divide(new BigInteger("100000000"));
                s = Math.floor(new Double(b1.toString()));
                showMoney.append(s.intValue()+"<b>亿</b>");
            }

            if(account.compareTo(new BigInteger("10000"))==1 || account.compareTo(new BigInteger("10000"))==0){
                BigInteger b1 = account.remainder(new BigInteger("100000000")).divide(new BigInteger("10000"));
                s = Math.floor(new Double(b1.toString()));
                if(s.intValue()>0) {
                    showMoney.append(s.intValue() + "<b>万</b>");
                }
            }
        }

        return showMoney.toString();
    }


    /**
     * 格式化数字转成单位：亿，万，余数。
     *
     * <p>
     *    <ul>
     *        <li>亿：hundredMillion；</li>
     *        <li>万：tenThousand；</li>
     *        <li>余数：remainder。</li>
     *    </ul>
     * </p>
     * @param number 传入数字。
     * @param isUser 是否是用户数量。
     * @return 亿，万，余数 的数量。
     */
    public static Map<String, String> formatNumber(BigDecimal number, boolean isUser) {
        String hundredMillion = "0";//亿
        String tenThousand = "0";//万
        String remainder = "0";//余数

        BigInteger account = number.toBigInteger();

        if (isUser) {
            if (account.compareTo(new BigInteger("10000")) == 1 || account.compareTo(new BigInteger("10000")) == 0) {
                BigInteger b1 = account.remainder(new BigInteger("100000000")).divide(new BigInteger("10000"));
                tenThousand = b1.toString();
            }
            if (account.compareTo(new BigInteger("1000")) == 1 || account.compareTo(new BigInteger("1000")) == 0) {
                BigInteger b1 = account.remainder(new BigInteger("10000")).divide(new BigInteger("1"));
                remainder = b1.toString();
            }
        } else {
            if (account.compareTo(new BigInteger("100000000")) == 1 || account.compareTo(new BigInteger("100000000")) == 0) {
                BigInteger b1 = account.divide(new BigInteger("100000000"));
                hundredMillion = b1.toString();
            }

            if (account.compareTo(new BigInteger("10000")) == 1 || account.compareTo(new BigInteger("10000")) == 0) {
                BigInteger b1 = account.remainder(new BigInteger("100000000")).divide(new BigInteger("10000"));
                tenThousand = b1.toString();
            }
        }
        Map<String, String> result = new HashMap<String, String>();
        result.put("hundredMillion", hundredMillion);
        result.put("tenThousand", tenThousand);
        result.put("remainder", remainder);
        return result;
    }
}
