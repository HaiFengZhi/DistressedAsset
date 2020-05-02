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

package com.distressed.asset.rocketmq.client;

import com.distressed.asset.common.result.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * RocketMQ消息发送接口。
 *
 * @author hongchao zhao at 2020-4-28 14:52
 */
@FeignClient(value = "rocketmq-server", contextId = "rocketMQService")
public interface RocketMQService {

    /**
     * RocketMQ发送顺序消息。
     *
     * @param msg       消息。
     * @param topic     主题。
     * @param tags      标签。
     * @param uniqueKey 唯一标识。
     * @return 发送结果。
     */
    @PostMapping("/sendOrderMsg")
    ResultBean<Boolean> sendOrderMsg(@RequestParam(value = "msg") String msg, @RequestParam(value = "topic") String topic, @RequestParam(value = "tags") String tags, @RequestParam(value = "uniqueKey") String uniqueKey);

    /**
     * RocketMQ发送非顺序同步消息。
     *
     * @param msg       消息。
     * @param topic     主题。
     * @param tags      标签。
     * @param uniqueKey 唯一标识。
     * @return 发送结果。
     */
    @PostMapping("/sendSyncMsg")
    ResultBean<Boolean> sendSyncMsg(@RequestParam(value = "msg") String msg, @RequestParam(value = "topic") String topic, @RequestParam(value = "tags") String tags, @RequestParam(value = "uniqueKey") String uniqueKey);

    /**
     * RocketMQ发送非顺序异步消息。
     *
     * @param msg       消息。
     * @param topic     主题。
     * @param tags      标签。
     * @param uniqueKey 唯一标识。
     * @return 发送结果。
     */
    @PostMapping("/sendAsyncMsg")
    ResultBean<Boolean> sendAsyncMsg(@RequestParam(value = "msg") String msg, @RequestParam(value = "topic") String topic, @RequestParam(value = "tags") String tags, @RequestParam(value = "uniqueKey") String uniqueKey);

    /**
     * RocketMQ发送非顺序单向消息，不必等待结果，普通消息推荐使用此方式。
     *
     * @param msg       消息。
     * @param topic     主题。
     * @param tags      标签。
     * @param uniqueKey 唯一标识。
     * @return 发送结果。
     */
    @PostMapping("/sendOnewayMsg")
    ResultBean<Boolean> sendOneWayMsg(@RequestParam(value = "msg") String msg, @RequestParam(value = "topic") String topic, @RequestParam(value = "tags") String tags, @RequestParam(value = "uniqueKey") String uniqueKey);


}
