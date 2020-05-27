package com.hegp.core.model;

import java.io.Serializable;

/**
 * 自定义响应结构
 *  200 业务办理成功
 *  300 业务办理失败(业务逻辑处理层)
 *  400 业务参数错误(接口参数错误)
 *  999 业务办理异常(数据库方面的异常)
 */

public class Result<T> implements Serializable {

    private static final long serialVersionUID = -4254726102616289056L;
    private Integer code;		// 响应业务状态
    private String msg;         // 短信服务返回的是这个字段
    private T data;		    // 响应中的数据

    public Result(){ }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 办理业务成功(有返回信息和数据)
    public static Result success() {
        return new Result(200, "成功", null);
    }

    // 办理业务成功(有返回信息和数据)
    public static Result success(Object data) {
        return new Result(200, "成功", data);
    }

    // 办理业务成功(有返回信息和数据)
    public static Result success(String msg, Object data) {
        return new Result(200, msg, data);
    }

    public static Result fail(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public static Result fail(int code, String msg) {
        return new Result(code, msg, null);
    }

    // 办理业务失败,不返回数据
    public static Result fail(String msg) {
        return new Result(499, msg, null);
    }

    public Result(Integer code, String msg, T result) {
        this.code = code;
        this.msg = msg;
    }

    public Result(String msg) {
        this.msg = msg;
    }

}
