package com.auiucloud.core.douyin.service;


import com.auiucloud.core.douyin.model.AppletUserInfo;
import com.auiucloud.core.douyin.model.DouyinApiResult;
import com.auiucloud.core.douyin.model.DyAppletCode2Session;

import java.util.List;

/**
 * @author dries
 **/
public interface DouyinAppletsService {

    String getAccessToken();

    DyAppletCode2Session getCode2Session(String code);

    AppletUserInfo getUserInfo(String sessionKey, String encryptedData, String iv);

    boolean checkText(String content);

    List<Integer> checkTextList(List<String> contents);

    String checkImage(String image);

    String checkBase64Image(String imageData);

    DouyinApiResult createOrder();

}
