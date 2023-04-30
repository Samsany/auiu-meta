package com.auiucloud.auth.feign;

import com.auiucloud.auth.dto.DouyinContentCheckDTO;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.feign.constant.FeignConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author dries
 **/
@FeignClient(value = FeignConstant.META_CLOUD_AUTH)
public interface IDouyinProvider {

    /**
     * 内容安全检测
     */
    @PostMapping(ProviderConstant.PROVIDER_DOUYIN_CHECK_TEXT)
    ApiResult<Boolean> checkText(@Validated @RequestBody DouyinContentCheckDTO checkDTO);

    /**
     * 内容安全检测
     */
    @PostMapping(ProviderConstant.PROVIDER_DOUYIN_CHECK_TEXTS)
    ApiResult<List<Integer>> checkTextList(@Validated @RequestBody DouyinContentCheckDTO checkDTO);

    /**
     * 图片安全检测
     *
     * @param checkDTO 检测内容
     */
    @PostMapping(ProviderConstant.PROVIDER_DOUYIN_CHECK_IMAGE)
    ApiResult<String> checkImage(@Validated @RequestBody DouyinContentCheckDTO checkDTO);

    /**
     * 图片安全检测
     *
     * @param checkDTO 检测内容
     */
    @PostMapping(ProviderConstant.PROVIDER_DOUYIN_CHECK_IMAGE_DATA)
    ApiResult<String> checkImageData(@Validated @RequestBody DouyinContentCheckDTO checkDTO);
}
