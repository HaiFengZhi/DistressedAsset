package com.distressed.asset.rocketmq.annotation;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * ClassName: RocketListener  
 *
 * @author Super.Zhao
 * @since JDK 1.8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface RocketListener {

	/**
	 * Message 所属的 Topic
	 * @return String
	 */
	String topic() default "";
	
	/**
	 * 是否为顺序消息
	 * @return Boolean
	 */

	boolean orderConsumer() default false;
	
	/**
	 * 消费模式，默认集群消费
	 * @return
	 */
	MessageModel messageModel() default MessageModel.CLUSTERING;


}
