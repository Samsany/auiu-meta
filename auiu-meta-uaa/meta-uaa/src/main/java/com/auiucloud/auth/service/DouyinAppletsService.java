package com.auiucloud.auth.service;

import com.auiucloud.auth.model.AppletCode2Session;
import com.auiucloud.auth.model.AppletUserInfo;
import com.auiucloud.auth.model.TextAntidirtResult;

/**
 * @author dries
 **/
public interface DouyinAppletsService {

    String getAccessToken();
    AppletCode2Session getCode2Session(String code);
    AppletUserInfo getUserInfo(String sessionKey, String encryptedData, String iv);

    boolean checkText(String content);
    boolean checkImage(String image);

}
