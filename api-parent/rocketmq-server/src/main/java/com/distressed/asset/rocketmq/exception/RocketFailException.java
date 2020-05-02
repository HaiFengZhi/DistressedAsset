package com.distressed.asset.rocketmq.exception;

/**
 * 消息处理失败, 消息重新队列
 * @author xifeng
 */
public class RocketFailException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RocketFailException(String message) {
		super(message);
	}

	public RocketFailException(Throwable cause) {
		super(cause);
	}

	public RocketFailException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
