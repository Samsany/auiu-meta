package com.auiucloud.core.log.feign;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.common.model.CommonLog;
import com.auiucloud.core.feign.constant.FeignConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author dries
 * @date 2021/12/24
 */
@FeignClient(value = FeignConstant.AUIU_MATE_CLOUD_ADMIN)
public interface ISysLogProvider {

    /**
     * 日志设置
     *
     * @param commonLog 　CommonLog对象
     * @return Result
     */
    @PostMapping(ProviderConstant.PROVIDER_LOG_SET)
    ApiResult<Boolean> set(@RequestBody CommonLog commonLog);

}
