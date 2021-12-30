package com.auiucloud.core.log.feign;

import com.auiucloud.core.common.api.ApiResponse;
import com.auiucloud.core.common.model.CommonLog;
import com.auiucloud.core.feign.constant.FeignConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author dries
 * @date 2021/12/24
 */
@FeignClient(value = FeignConstant.AUIU_MATE_CLOUD_SYSTEM)
public interface ISysLogService {

    /**
     * 日志设置
     *
     * @param commonLog 　CommonLog对象
     * @return Result
     */
    @PostMapping("/admin/log/set")
    ApiResponse<Boolean> set(@RequestBody CommonLog commonLog);

}
