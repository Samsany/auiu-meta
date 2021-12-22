package com.auiucloud.core.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author dries
 * @date 2021/12/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = -3018546779590723199L;

    @ApiModelProperty(value = "状态码")
    private int code;

    @ApiModelProperty(value = "消息内容")
    private String message;

    @ApiModelProperty(value = "时间戳")
    private long time;

    @ApiModelProperty(value = "业务数据")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /*** 成功返回结果, 无参 */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<T>(ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMessage(),
                System.currentTimeMillis(),
                null);
    }

    /**
     * 成功返回结果
     *
     * @param message 提示信息
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<T>(ResultCode.SUCCESS.getCode(), message, System.currentTimeMillis(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), System.currentTimeMillis(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>(ResultCode.SUCCESS.getCode(), message, System.currentTimeMillis(), data);
    }

    /*** 失败返回结果 */
    public static <T> ApiResponse<T> failed() {
        return fail(ResultCode.USER_ERROR_A0500);
    }

    /**
     * 失败返回结果, 无 data
     *
     * @param resultCode 返回码
     */
    public static <T> ApiResponse<T> fail(IResultCode resultCode) {
        return new ApiResponse<T>(resultCode.getCode(), resultCode.getMessage(), System.currentTimeMillis(), null);
    }

    /**
     * 失败返回结果，自定义消息提示，无 data
     *
     * @param message 提示信息
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<T>(code, message, System.currentTimeMillis(), null);
    }

    /**
     * 失败返回结果，自定义消息提示，无 data
     *
     * @param message 提示信息
     */
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<T>(ResultCode.ERROR.getCode(), message, System.currentTimeMillis(), null);
    }

    /**
     * 失败返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ApiResponse<T> fail(IResultCode resultCode, T data) {
        return new ApiResponse<T>(resultCode.getCode(), resultCode.getMessage(), System.currentTimeMillis(), data);
    }

    /*** 未登录返回结果 */
    public static <T> ApiResponse<T> unauthorized(T data) {
        return fail(ResultCode.USER_ERROR_A0230, data);
    }

    /*** 未授权返回结果 */
    public static <T> ApiResponse<T> forbidden(T data) {
        return fail(ResultCode.USER_ERROR_A0301, data);
    }

    public boolean successful() {
        return code == ResultCode.SUCCESS.getCode();
    }

}
