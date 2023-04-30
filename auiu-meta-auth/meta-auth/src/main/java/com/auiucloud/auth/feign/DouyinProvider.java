package com.auiucloud.auth.feign;

import com.auiucloud.auth.config.DouyinAppletsConfiguration;
import com.auiucloud.auth.dto.DouyinContentCheckDTO;
import com.auiucloud.auth.service.DouyinAppletsService;
import com.auiucloud.core.common.api.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dries
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "抖音服务端接口调用")
public class DouyinProvider implements IDouyinProvider {

    /**
     * 内容安全检测
     *
     * @param checkDTO 检测内容
     */
    @Override
    public ApiResult<Boolean> checkText(DouyinContentCheckDTO checkDTO) {
        DouyinAppletsService douyinAppletService = DouyinAppletsConfiguration.getDouyinAppletService(checkDTO.getAppId());
        return ApiResult.condition(douyinAppletService.checkText(checkDTO.getContent()));
    }

    /**
     * 内容安全检测
     *
     * @param checkDTO 检测内容
     */
    @Override
    public ApiResult<List<Integer>> checkTextList(DouyinContentCheckDTO checkDTO) {
        DouyinAppletsService douyinAppletService = DouyinAppletsConfiguration.getDouyinAppletService(checkDTO.getAppId());
        return ApiResult.data(douyinAppletService.checkTextList(checkDTO.getContents()));
    }

    /**
     * 图片安全检测
     *
     * @param checkDTO 检测内容
     */
    @Override
    public ApiResult<String> checkImage(DouyinContentCheckDTO checkDTO) {
        DouyinAppletsService douyinAppletService = DouyinAppletsConfiguration.getDouyinAppletService(checkDTO.getAppId());
        return ApiResult.data(douyinAppletService.checkImage(checkDTO.getImage()));
    }

    /**
     * 图片安全检测
     *
     * @param checkDTO 检测内容
     */
    @Override
    public ApiResult<String> checkImageData(DouyinContentCheckDTO checkDTO) {
        DouyinAppletsService douyinAppletService = DouyinAppletsConfiguration.getDouyinAppletService(checkDTO.getAppId());
        return ApiResult.data(douyinAppletService.checkImageData(checkDTO.getImageData()));
    }
}
