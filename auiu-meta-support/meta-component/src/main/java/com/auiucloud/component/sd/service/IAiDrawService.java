package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.AiDrawInfo;
import com.auiucloud.component.sd.domain.SdDrawResult;
import com.auiucloud.component.sd.dto.SdImg2ImgConfigDTO;
import com.auiucloud.component.sd.dto.SdImg2ImgDTO;
import com.auiucloud.component.sd.dto.SdTxt2ImgConfigDTO;
import com.auiucloud.component.sd.dto.SdTxt2ImgDTO;
import com.auiucloud.core.common.model.WsMsgModel;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

/**
 * Ai绘画接口
 *
 * @author dries
 **/
public interface IAiDrawService {

    void sdText2Img(SdTxt2ImgConfigDTO params);

    void sdImg2Img(SdImg2ImgConfigDTO params);

    SdTxt2ImgConfigDTO generatorSdTxt2ImgParams(SdTxt2ImgDTO sdDrawParam);

    SdImg2ImgConfigDTO generatorSdImg2ImgParams(SdImg2ImgDTO sdDrawParam);

    List<Long> saveTxt2ImgRecord(String config, SdTxt2ImgConfigDTO params);

    List<Long> saveImg2ImgRecord(String config, SdImg2ImgConfigDTO params);

    void sdTxt2ImgHandleMessage(WebSocketSession session, WsMsgModel payload) throws IOException;

    void sdImg2ImgHandleMessage(WebSocketSession session, WsMsgModel payload) throws IOException;

    void updateDrawStatusByIds(String drawIds, Integer status);

    List<AiDrawInfo> updateAiDrawResultByIds(SdTxt2ImgConfigDTO txt2ImgConfig, SdDrawResult sdDrawResult);

    List<AiDrawInfo> updateAiDrawResultByIds(String platform, String appId, String drawIds, String taskId, Long userId, SdDrawResult sdDrawResult);

}
