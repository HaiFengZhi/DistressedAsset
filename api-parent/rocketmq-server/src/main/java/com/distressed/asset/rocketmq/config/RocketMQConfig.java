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

package com.distressed.asset.rocketmq.config;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.distressed.asset.common.utils.AnnotatedUtil;
import com.distressed.asset.rocketmq.annotation.MessageHandle;
import com.distressed.asset.rocketmq.annotation.RocketListener;
import com.distressed.asset.rocketmq.exception.RocketFailException;
import com.distressed.asset.rocketmq.exception.RocketSuccessException;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * RocketMQ启动配置。
 *
 * @author hongchao zhao at 2020-4-28 15:02
 */
@Configuration
public class RocketMQConfig implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(RocketMQConfig.class);

    /**
     * 上下文信息
     * */
    private ApplicationContext applicationContext;

    @Value("${rocketmq.producer.group}")
    private String producerGroup;
    @Value("${rocketmq.producer.sendMessageTimeout}")
    private int producerTimeout;
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private int retryTimesWhenSendFailed;
    @Value("${rocketmq.name-server}")
    private String nameServer;
    @Value("${rocketmq.consumer.group}")
    private String consumerGroup;
    @Value("${rocketmq.consumer.maxReconsumeTimes}")
    private int maxReconsumeTimes;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.consumeTimeout}")
    private int consumeTimeout;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;

    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        LOG.info("ROCKET初始化启动生产者(普通)！");
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");
        producer.setSendMsgTimeout(producerTimeout);
        producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        try {
            producer.start();
            LOG.info(String.format("Rocketmq Producer 启动成功! GroupName:[%s],NamesrvAddr:[%s]", this.producerGroup, this.nameServer));
        } catch (MQClientException e) {
            LOG.error("ROCKETMQ PRODUCER 启动失败 {},{}", e.getMessage(), e);
        }
        return producer;
    }

    //顺序消息消费，带事务方式（应用可控制Offset什么时候提交）
    @Bean
    public DefaultMQPushConsumer defaultMQPushConsumer() throws MQClientException {
        LOG.info("ROCKET初始化启动顺序消费者！");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(nameServer);
        consumer.setMaxReconsumeTimes(maxReconsumeTimes);
        //设置消费者消费并行线程
//        consumer.setConsumeThreadMin(consumeThreadMin);
//        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setConsumeTimeout(consumeTimeout);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);

        Map<String, Object> rocketBeanClass = applicationContext.getBeansWithAnnotation(RocketListener.class);
        rocketBeanClass.forEach((beanName, topicClass) -> {
            RocketListener rocketListener = topicClass.getClass().getAnnotation(RocketListener.class);
            if (rocketListener.orderConsumer()) {
                String topic = rocketListener.topic();
                MessageListenerOrderly messageListener = new MessageListenerOrderly() {
                    @Override
                    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                        MessageExt message = null;
                        //这里实际上只会产生一条消息
                        for (MessageExt messageExt : msgs) {
                            message = messageExt;
                        }
                        if (message != null) {
                            String messageJson = new String(message.getBody());
                            String tags = message.getTags();
                            try {
                                rocketBeanClass.forEach((beanName, topicClass) -> {
                                    AnnotatedUtil.getMethodAndAnnotation(topicClass, MessageHandle.class).forEach((method, consumerListener) -> {
                                        if (ObjectUtil.equal("all", tags) || ObjectUtil.contains(tags, consumerListener.tag())) {
                                            ReflectUtil.invoke(topicClass, method, messageJson);
                                        }
                                    });
                                });
                            } catch (UtilException e) {
                                InvocationTargetException targetException = (InvocationTargetException) e.getCause();
                                if (targetException.getTargetException() instanceof RocketFailException) {
                                    LOG.info("消息处理失败, 消息重新队列" + "[" + e.getCause().getMessage() + "]");
                                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                                } else if (targetException.getTargetException() instanceof RocketSuccessException) {
                                    LOG.info("消息处理失败, 消息移除队列" + "[" + e.getCause().getMessage() + "]");
                                    return ConsumeOrderlyStatus.SUCCESS;
                                } else if (targetException.getTargetException() instanceof RuntimeException) {
                                    LOG.info("消息处理失败, 消息移除队列" + "[" + e.getCause().getMessage() + "]");
                                    return ConsumeOrderlyStatus.SUCCESS;
                                }
                            }
                        }
                        LOG.info("消息处理成功");
                        return ConsumeOrderlyStatus.SUCCESS;
                    }
                };

                try {
                    consumer.subscribe(topic, "*");
                    consumer.registerMessageListener(messageListener);
                    LOG.info("顺序消息topic[{}]已绑定", topic);
                } catch (Exception e) {
                    LOG.error("Rocketmq Consumer 启动失败 !!! groupName:{},topics:{},namesrvAddr:{}", consumerGroup, topic, nameServer, e);
                }
            }
        });
        consumer.start();
        LOG.info("Rocketmq Consumer 启动成功 !!! groupName:{},namesrvAddr:{}", consumerGroup, nameServer);
        return consumer;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
