package com.auiucloud.component.sd.component;

import com.auiucloud.component.cms.props.SdConstants;
import com.auiucloud.component.sd.domain.SdDrawResult;
import com.auiucloud.component.sd.domain.SdImg2ImgParams;
import com.auiucloud.component.sd.domain.SdTxt2ImgParams;
import com.auiucloud.component.sd.service.IAiDrawFactoryService;
import com.auiucloud.component.sd.vo.SdProgressVO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.web.utils.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author dries
 **/
@Slf4j
@Service
public class AiDrawFactory implements IAiDrawFactoryService {

    /**
     * 文生图
     *
     * @param txt2ImgParams 参数
     * @return Object
     */
    @Override
    public ApiResult<SdDrawResult> sdText2Img(SdTxt2ImgParams txt2ImgParams) {
        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<SdDrawResult> post = RestTemplateUtil.post(this.url + SdConstants.text2Img, headers, txt2ImgParams, SdDrawResult.class);
            return ApiResult.data(post.getBody());
        } catch (Exception e) {
            // log.error("====================> {}", e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    /**
     * 文生图
     *
     * @param img2ImgParams 参数
     * @return Object
     */
    @Override
    public ApiResult<SdDrawResult> sdImg2Img(SdImg2ImgParams img2ImgParams) {
        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<SdDrawResult> post = RestTemplateUtil.post(this.url + SdConstants.img2Img, headers, img2ImgParams, SdDrawResult.class);
            return ApiResult.data(post.getBody());
        } catch (Exception e) {
            // log.error("====================> {}", e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    /**
     * 进度条
     *
     * @return SdProgressVO
     */
    @Override
    public SdProgressVO getSdProgress() {
        try {
            // 定时任务的逻辑
            return RestTemplateUtil.get(this.url + SdConstants.progress + "?skip_current_image=false", SdProgressVO.class).getBody();
        } catch (Exception e) {
            log.error("获取生图进度异常：{}", e.getMessage());
            return null;
        }
    }

    private String appId;
    private String appSecret;
    private String url;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
