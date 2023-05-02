package com.auiucloud.auth.service;

/**
 * @author dries
 **/
public interface SmsCodeSenderService {

    void send(String key, String mobile, String params);

}
