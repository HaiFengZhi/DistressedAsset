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

package com.distressed.asset.rocketmq.listener;

import com.distressed.asset.rocketmq.annotation.MessageHandle;
import com.distressed.asset.rocketmq.annotation.RocketListener;
import com.distressed.asset.rocketmq.message.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RocketMQ测试监听方法。
 *
 * @author hongchao zhao at 2020-4-28 17:54
 */
@RocketListener(topic = MessageConstants.Topic.TOPIC_DEMO_TEST,orderConsumer = true)
public class DemoRocketListener {

    private static final Logger LOG = LoggerFactory.getLogger(DemoRocketListener.class);

    @MessageHandle(tag = MessageConstants.Tag.TAG_SAY_HELLO)
    public void test(String message){
        LOG.debug("RocketMQ监听消息处理：" + message);

    }

}
