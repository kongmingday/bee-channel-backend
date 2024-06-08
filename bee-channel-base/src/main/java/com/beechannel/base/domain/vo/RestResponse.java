package com.beechannel.base.domain.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author eotouch
 * @version 1.0
 * @description 通用结果类型
 * @date 2022/9/13 14:44
 */

@Data
@ToString
public class RestResponse<T> {

    /**
     * 响应编码,200为正常,-1错误
     */
    private int code;

    /**
     * 响应提示信息
     */
    private String msg;

    /**
     * 响应内容
     */
    private T result;


    public RestResponse() {
        this(200, "success");
    }

    public RestResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 错误信息的封装
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> validFail(String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(-1);
        response.setMsg(msg);
        return response;
    }

    public static <T> RestResponse<T> validFail() {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(-1);
        return response;
    }

    public static <T> RestResponse<T> validFail(int code, String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static <T> RestResponse<T> validFail(T result, String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(-1);
        response.setResult(result);
        response.setMsg(msg);
        return response;
    }


    /**
     * 添加正常响应数据（包含响应内容）
     *
     * @return RestResponse Rest服务封装相应数据
     */
    public static <T> RestResponse<T> success(String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setMsg(msg);
        return response;
    }

    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<>();
        response.setMsg("request success");
        response.setResult(result);
        return response;
    }

    public static <T> RestResponse<T> success(T result, String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setResult(result);
        response.setMsg(msg == null ? "request success" : msg);
        return response;
    }

    /**
     * 添加正常响应数据（不包含响应内容）
     *
     * @return RestResponse Rest服务封装相应数据
     */
    public static RestResponse<Boolean> success() {
        RestResponse<Boolean> response = new RestResponse<>();
        response.setMsg("request success");
        response.setResult(true);
        return response;
    }


    public Boolean hasSuccessful() {
        return this.code == 200;
    }

}