package com.auiucloud.admin.feign;

import com.auiucloud.admin.modules.system.domain.SysLog;
import com.auiucloud.admin.modules.system.service.ISysLogService;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.common.model.CommonLog;
import com.auiucloud.core.log.feign.ISysLogProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dries
 * @date 2021/12/30
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "日志远程调用")
public class SysLogProvider implements ISysLogProvider {

    private final ISysLogService sysLogService;

    @Override
    @PostMapping(ProviderConstant.PROVIDER_LOG_SET)
    @Operation(summary = "日志设置", description = "日志设置")
    public ApiResult<Boolean> set(CommonLog commonLog) {

        SysLog sysLog = new SysLog();
        BeanUtils.copyProperties(commonLog, sysLog);

        return ApiResult.condition(sysLogService.save(sysLog));
    }

}
