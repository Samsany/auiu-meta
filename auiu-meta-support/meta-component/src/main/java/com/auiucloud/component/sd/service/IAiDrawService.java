package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.AiDrawInfo;
import com.auiucloud.component.sd.domain.SdDrawResult;
import com.auiucloud.component.sd.dto.SdTxt2ImgDTO;
import com.auiucloud.component.sd.dto.SdTxt2ImgConfigDTO;
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

    void sdText2Img(SdTxt2ImgConfigDTO paramVO);

    SdTxt2ImgConfigDTO generatorSdTxt2ImgParams(SdTxt2ImgDTO sdDrawParam);

    List<Long> saveTxt2ImgRecord(String config, SdTxt2ImgConfigDTO params);

    void sdTxt2ImgHandleMessage(WebSocketSession session, WsMsgModel payload) throws IOException;

    void updateDrawStatusByIds(String drawIds, Integer status);

    List<AiDrawInfo> updateAiDrawResultByIds(SdTxt2ImgConfigDTO txt2ImgConfig, SdDrawResult sdDrawResult);
}
