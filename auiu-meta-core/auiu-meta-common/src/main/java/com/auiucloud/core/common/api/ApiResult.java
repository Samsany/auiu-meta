package com.auiucloud.core.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dries
 * @date 2021/12/21
 */
@Data
@Schema(name = "统一响应报文")
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = -3018546779590723199L;

    @Schema(description = "状态码", required = true)
    private int code;

    @Schema(description = "消息内容", required = true)
    private String message;

    @Schema(description = "时间戳", required = true)
    private long time;

    @Schema(description = "业务数据")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResult() {
        this.time = System.currentTimeMillis();
    }

    public ApiResult(IResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public ApiResult(IResultCode resultCode, String message) {
        this(resultCode.getCode(), message, null);
    }

    public ApiResult(IResultCode resultCode, T data) {
        this(resultCode.getCode(), resultCode.getMessage(), data);
    }

    public ApiResult(IResultCode resultCode, String message, T data) {
        this(resultCode.getCode(), message, data);
    }

    public ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.time = System.currentTimeMillis();
    }

    /*** 成功返回结果, 默认无参 */
    public static <T> ApiResult<T> success() {
        return success(ResultCode.SUCCESS);
    }

    public static <T> ApiResult<T> success(IResultCode resultCode) {
        return new ApiResult<>(resultCode);
    }

    /**
     * 成功返回结果
     *
     * @param message 提示信息
     */
    public static <T> ApiResult<T> success(String message) {
        return new ApiResult<>(ResultCode.SUCCESS, message);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ApiResult<T> data(T data) {
        return new ApiResult<>(ResultCode.SUCCESS, data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> ApiResult<T> data(String message, T data) {
        return new ApiResult<>(ResultCode.SUCCESS, message, data);
    }

    /**
     * 成功返回结果
     *
     * @param code    错误编码
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> ApiResult<T> data(int code, String message, T data) {
        return new ApiResult<>(code, message, data);
    }

    /*** 失败返回结果 */
    public static <T> ApiResult<T> fail() {
        return fail(ResultCode.USER_ERROR_A0500);
    }

    /**
     * 失败返回结果, 无 data
     *
     * @param resultCode 返回码
     */
    public static <T> ApiResult<T> fail(IResultCode resultCode) {
        return new ApiResult<>(resultCode);
    }

    /**
     * 失败返回结果，自定义消息提示，无 data
     *
     * @param message 提示信息
     */
    public static <T> ApiResult<T> fail(int code, String message) {
        return new ApiResult<>(code, message, null);
    }

    /**
     * 失败返回结果，自定义消息提示，无 data
     *
     * @param message 提示信息
     */
    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(ResultCode.USER_ERROR_A0500, message);
    }

    /**
     * 失败返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ApiResult<T> fail(IResultCode resultCode, T data) {
        return new ApiResult<>(resultCode, data);
    }

    public static <T> ApiResult<T> condition(boolean flag) {
        return flag ? success(ResultCode.SUCCESS.getMessage()) : fail(ResultCode.USER_ERROR_A0500.getMessage());
    }

    public static <T> ApiResult<T> condition(String message, boolean flag) {
        return flag ? success(ResultCode.SUCCESS.getMessage()) : fail(message);
    }

    public boolean successful() {
        return code == ResultCode.SUCCESS.getCode();
    }


}
