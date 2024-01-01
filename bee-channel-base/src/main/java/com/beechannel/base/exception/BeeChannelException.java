package com.beechannel.base.exception;


import java.io.Serializable;

/**
 * @description Bee_Channel异常类
 * @author eotouch
 * @date 2023/1/31 11:29
 * @version 1.0
 */
public class BeeChannelException extends CommonException implements Serializable {

	private static final long serialVersionUID = 5565760508056698922L;

	public BeeChannelException() {
		super();
	}

	public BeeChannelException(String errMessage) {
		super(errMessage);
	}

	public static void cast(CommonError commonError){
		throw new BeeChannelException(commonError.getErrMessage());
	}
	public static void cast(String errMessage){
		throw new BeeChannelException(errMessage);
	}

}
