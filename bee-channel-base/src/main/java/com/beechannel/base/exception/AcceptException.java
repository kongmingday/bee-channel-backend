package com.beechannel.base.exception;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2024/01/01 23:11
 * @Version 1.0
 */
public class AcceptException extends CommonException implements Serializable {

    private static final long serialVersionUID = 5565760508056698922L;

    private boolean result;

    public AcceptException() {
        super();
        this.result = false;
    }

    public AcceptException(String errMessage) {
        super(errMessage);
        this.result = false;
    }

    public AcceptException(String errMessage, boolean result) {
        super(errMessage);
        this.result = result;
    }

    public static void cast(CommonError commonError){
        throw new AcceptException(commonError.getErrMessage());
    }
    public static void cast(String errMessage){
        throw new AcceptException(errMessage);
    }

    public static void cast(String errMessage, boolean result){
        throw new AcceptException(errMessage, result);
    }

    public boolean isResult() {
        return result;
    }
}
