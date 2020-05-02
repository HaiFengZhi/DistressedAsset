package com.distressed.asset.rocketmq.exception;

/**
 * 消息处理失败, 消息移除队列
 * @author xifeng
 */
public class RocketSuccessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RocketSuccessException(String message) {
		super(message);
	}

	public RocketSuccessException(Throwable cause) {
		super(cause);
	}

	public RocketSuccessException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
