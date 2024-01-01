package com.beechannel.base.exception;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2024/01/01 23:13
 * @Version 1.0
 */
public class CommonException extends RuntimeException{

    private static final long serialVersionUID = 5565760508056698922L;

    private String errMessage;

    public CommonException() {
        super();
    }

    public CommonException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

}

