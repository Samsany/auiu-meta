package com.auiucloud.auth.model;

import lombok.Data;

/**
 * @author dries
 **/
@Data
public class DouyinAuthResult <T> {

    private int err_no;

    private String err_tips;

    private T data;
}
