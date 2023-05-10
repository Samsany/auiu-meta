package com.auiucloud.core.douyin.model;

import lombok.Data;

/**
 * @author dries
 **/
@Data
public class DouyinApiResult<T> {

    private int err_no;

    private String err_tips;

    private T data;
}
