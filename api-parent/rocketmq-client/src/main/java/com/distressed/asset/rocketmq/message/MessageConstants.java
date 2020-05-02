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

package com.distressed.asset.rocketmq.message;

/**
 * RocketMQ消息区分键。
 *
 * @author hongchao zhao at 2020-4-28 17:20
 */
public class MessageConstants {

    //todo:需要先在RocketMQ控制台新建对应的主题
    //RocketMQ的主题
    public final class Topic {
        public static final String TOPIC_DEMO_TEST = "TOPIC_DEMO_TEST";
    }

    //RocketMQ的标签
    public final class Tag {

        public static final String TAG_SAY_HELLO = "TAG_SAY_HELLO";

    }

}
