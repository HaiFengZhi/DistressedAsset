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

package com.distressed.asset.rocketmq.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.rocketmq.client.RocketMQService;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * RocketMQ消息发送实现类。
 *
 * @author hongchao zhao at 2020-4-28 17:42
 */
@RestController
public class RocketMQServiceImpl implements RocketMQService {

    private static final Logger LOG = LoggerFactory.getLogger(RocketMQServiceImpl.class);

    @Resource
    private DefaultMQProducer defaultMQProducer;


    @Override
    public ResultBean<Boolean> sendOrderMsg(String msg, String topic, String tags, String uniqueKey) {
        LOG.debug("发送【顺序】消息，会有发送结果返回，【{}】【{}】【{}】【{}】", msg, topic, tags, uniqueKey);
        try {
            Message message = new Message(topic, tags, uniqueKey, msg.getBytes());
            // 发送消息时，需要实现MessageQueueSelector ， 用来选择合适的queue
            //获取当前主题下共有多少队列，随机一个队列去发布消息
            int count = defaultMQProducer.fetchPublishMessageQueues(topic).size();
            //同样的下标会让所有消息发送到同一队列
            Integer orderIndex = RandomUtils.nextInt(count);
            LOG.debug("当前消息队列数：【{}】，分配到队列【{}】", count, orderIndex);
            SendResult sendResult = defaultMQProducer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    LOG.debug("MessageQueue数量：" + mqs.size());
                    Integer id = (Integer) arg;
                    //要保证消息顺序不混乱，必须在生产时将消息push到同一个队列
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderIndex);

            if (ObjectUtil.isNotNull(sendResult)) {
                LOG.info(new Date() + " 发送MQ消息成功. Topic 是:" + message.getTopic() + " Tag 是:" + tags + " 消息ID是: " + sendResult.getMsgId());
            } else {
                LOG.warn(".sendResult is null.........");
            }
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            return ResultBean.failed(e.getMessage());
        }
        return ResultBean.success();
    }

    @Override
    public ResultBean<Boolean> sendSyncMsg(String msg, String topic, String tags, String uniqueKey) {
        LOG.debug("发送【普通同步】消息，会有发送结果返回，【{}】【{}】【{}】【{}】", msg, topic, tags, uniqueKey);
        try {
            Message message = new Message(topic, tags, uniqueKey, msg.getBytes());
            SendResult sendResult = defaultMQProducer.send(message);
            LOG.info("【普通同步】消息传输成功，返回【{}】", JSON.toJSONString(sendResult));
        } catch (MQClientException | RemotingException | InterruptedException | MQBrokerException e) {
            e.printStackTrace();
            return ResultBean.failed(e.getMessage());
        }
        return ResultBean.success();
    }

    @Override
    public ResultBean<Boolean> sendAsyncMsg(String msg, String topic, String tags, String uniqueKey) {
        LOG.debug("发送【普通异步】消息，会有返回结果，【{}】【{}】【{}】【{}】", msg, topic, tags, uniqueKey);
        try {
            Message message = new Message(topic, tags, uniqueKey, msg.getBytes());
            defaultMQProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    LOG.info("【普通异步】消息传输成功，返回【{}】", JSON.toJSONString(sendResult));
                }

                @Override
                public void onException(Throwable e) {
                    LOG.error("【普通异步】消息传输失败", e);
                }
            });
        } catch (MQClientException | RemotingException | InterruptedException e) {
            return ResultBean.failed(e.getMessage());
        }
        return ResultBean.success();
    }

    @Override
    public ResultBean<Boolean> sendOneWayMsg(String msg, String topic, String tags, String uniqueKey) {
        LOG.debug("发送【普通单向】消息，无需要等待结果，【{}】【{}】【{}】【{}】", msg, topic, tags, uniqueKey);
        try {
            Message message = new Message(topic, tags, uniqueKey, msg.getBytes());
            defaultMQProducer.sendOneway(message);
        } catch (MQClientException | RemotingException | InterruptedException e) {
            return ResultBean.failed(e.getMessage());
        }
        return ResultBean.success();
    }
}
