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

import com.online.shop.common.captcha.CaptchaUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 画图工具类。
 *
 * @author Elvis.Huang at 2015/08/24 13：05
 */
public final class GraphicsUtils {

    private static final int width = 100;
    private static final int height = 30;
    private static final HttpHeaders HTTP_HEADERS;
    private static final Random RANDOM;

    static {
        HTTP_HEADERS = new HttpHeaders();
        HTTP_HEADERS.set("Pragma", "No-cache");
        HTTP_HEADERS.set("Cache-Control", "No-cache");
        HTTP_HEADERS.setDate("Expires", 0);
        HTTP_HEADERS.setContentType(MediaType.IMAGE_JPEG);
        RANDOM = new Random();
    }

    private GraphicsUtils() {
        super();
    }

    /**
     * 绘制图片验证码。
     *
     * <p>
     *     结合Spring MVC一起使用，通过Spring MVC的{@link ResponseEntity}对象
     *     返回。
     * </p>
     *
     * @param imageCode 验证码(包含英文字母和数字)。
     * @return {@link ResponseEntity}，图片验证码的二进制字节码。
     */
    public static ResponseEntity<byte[]> generateImageCodeForSpringMVC(String imageCode) {
//        return new ResponseEntity<byte[]>(generateImageCode(imageCode, new String[]{"Times New Roman", "Algerian", "Axure Handwriting", "Rockwell"}), HTTP_HEADERS, HttpStatus.OK);
//        return new ResponseEntity<byte[]>(generateImageCodeByInterference(imageCode), HTTP_HEADERS, HttpStatus.OK);
        return new ResponseEntity<byte[]>(generateImageCodeByInterference(imageCode, null), HTTP_HEADERS, HttpStatus.OK);
    }

    /**
     * 绘制中文图片验证码。
     *
     * @param imageCode 验证码（包含中文）。
     * @return {@link ResponseEntity}，图片验证码的二进制字节码。
     */
    public static ResponseEntity<byte[]> generateChineseImageCodeForSpringMVC(String imageCode) {
        //备选字体：宋体，黑体，楷体，隶书。
        return new ResponseEntity<byte[]>(generateImageCode(imageCode, new String[]{ "\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66" }), HTTP_HEADERS, HttpStatus.OK);
    }


    /**
     * 绘制图片验证码。
     *
     * @param imageCode 验证码。
     * @param fonts     备选字体集合。
     * @return {@link ResponseEntity}，图片验证码的二进制字节码。
     */
    public static byte[] generateImageCode(String imageCode, String[] fonts) {
        if (StringUtils.isEmpty(imageCode) || CommonUtils.isBlank(fonts)) {
            return null;
        }

        char[] code = imageCode.toCharArray();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255)));

        //String[] fonts = new String[]{"Times New Roman", "Algerian", "Axure Handwriting", "Rockwell"};
        // 生成随机数,并将随机数字转换为字母
        for (int i = 0; i < code.length; i++) {
            g.setFont(new Font(fonts[RANDOM.nextInt(fonts.length)], Font.BOLD, 25));
            char ch = code[i];
            int px = RANDOM.nextInt(5);
            g.drawString(String.valueOf(ch), 25 * i + px + 3, 20 + px);
            // 画随机线
            int x = RANDOM.nextInt(width - 2);
            g.drawOval(x, px * 6, px * 2, 2);
        }
        g.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPEG", out);
            return out.toByteArray();
        } catch (IOException ex) {
            return out.toByteArray();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 加入干扰字符的验证码。
     * @param imageCode 验证码。
     * @return  {@link ResponseEntity}，图片验证码的二进制字节码。
     */
    public static byte[] generateImageCodeByInterference(String imageCode){
        ThreadLocalRandom r = ThreadLocalRandom.current();

        int count = imageCode.length();
        int fontSize = 180; //code的字体大小
        int fontMargin = fontSize/4; //字符间隔
        int width = (fontSize+fontMargin)*count+fontMargin; //图片长度
        int height = (int) (fontSize*1.2); //图片高度，根据字体大小自动调整；调整这个系数可以调整字体占图片的比例
        int avgWidth = width/count; //字符平均占位宽度
        int maxDegree = 20; //最大旋转度数

        //背景颜色
        Color bkColor = Color.WHITE;
        //验证码的颜色
        Color[] catchaColor = {Color.MAGENTA, Color.BLACK, Color.BLUE, Color.DARK_GRAY, Color.RED};

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        //填充底色为灰白
        g.setColor(bkColor);
        g.fillRect(0, 0, width, height);

        //画边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width-1, height-1);

        //画干扰字母、数字
        int dSize = fontSize/2; //调整分母大小以调整干扰字符大小
        Font font = new Font("Fixedsys", Font.PLAIN, dSize);
        g.setFont(font);
        int dNumber = width*height/dSize/dSize;//根据面积计算干扰字母的个数
        for(int i=0; i<dNumber; i++){
            char d_code = VerifyCodeUtils.VERIFY_CODES[r.nextInt(VerifyCodeUtils.VERIFY_CODES.length)];
            g.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
            g.drawString(String.valueOf(d_code), r.nextInt(width), r.nextInt(height));
        }

        //开始画验证码：

        // 创建字体
        font = new Font(Font.MONOSPACED, Font.ITALIC|Font.BOLD, fontSize);
        // 设置字体
        g.setFont(font);

        for(int i=0; i<count; i++){
            char c = imageCode.charAt(i);
            g.setColor(catchaColor[r.nextInt(catchaColor.length)]);//随机选取一种颜色

            //随机旋转一个角度[-maxDegre, maxDegree]
            int degree = r.nextInt(-maxDegree, maxDegree+1);

            //偏移系数，和旋转角度成反比，以避免字符在图片中越出边框
            double offsetFactor = 1-(Math.abs(degree)/(maxDegree+1.0));//加上1，避免出现结果为0

            g.rotate(degree * Math.PI / 180); //旋转一个角度
            int x = (int) (fontMargin + r.nextInt(avgWidth-fontSize)*offsetFactor); //横向偏移的距离
//            int y = (int) (fontSize + r.nextInt(height-fontSize)*offsetFactor); //上下偏移的距离

            g.drawString(String.valueOf(c), x, 150); //x,y是字符的左下角，偏离原点的距离！！！

            g.rotate(-degree * Math.PI / 180); //画完一个字符之后，旋转回原来的角度
            g.translate(avgWidth, 0);//移动到下一个画画的原点

            //X、Y坐标在合适的范围内随机，不旋转：
            //g.drawString(String.valueOf(c), width/count*i+r.nextInt(width/count-fontSize), fontSize+r.nextInt(height-fontSize));
        }

        g.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPEG", out);
            return out.toByteArray();
        } catch (IOException ex) {
            return out.toByteArray();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 加入干扰字符的验证码。
     * @param imageCode 验证码。
     * @param type      显示类型。
     * @return  {@link ResponseEntity}，图片验证码的二进制字节码。
     */
    public static byte[] generateImageCodeByInterference(String imageCode, Integer type) {
        if (type == null) {
            type = ThreadLocalRandom.current().nextInt(0, 20);
        }
        return CaptchaUtil.outputImageBytes(100, 40, imageCode, type);
    }
}
