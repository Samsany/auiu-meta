package com.auiucloud.auth.service;

import com.auiucloud.auth.model.AppletCode2Session;
import com.auiucloud.auth.model.AppletUserInfo;

import java.util.List;

/**
 * @author dries
 **/
public interface DouyinAppletsService {

    String getAccessToken();
    AppletCode2Session getCode2Session(String code);
    AppletUserInfo getUserInfo(String sessionKey, String encryptedData, String iv);

    boolean checkText(String content);
    List<Integer> checkTextList(List<String> contents);

    String checkImage(String image);
    String checkImageData(String imageData);

}
