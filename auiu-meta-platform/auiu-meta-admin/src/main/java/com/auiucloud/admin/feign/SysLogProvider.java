package com.auiucloud.admin.feign;

import com.auiucloud.admin.domain.SysLog;
import com.auiucloud.admin.service.ISysLogService;
import com.auiucloud.core.common.api.ApiResponse;
import com.auiucloud.core.common.constant.ProviderConstant;
import com.auiucloud.core.common.model.CommonLog;
import com.auiucloud.core.log.feign.ISysLogProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "日志远程调用")
public class SysLogProvider implements ISysLogProvider {

    private final ISysLogService sysLogService;

    @Override
    @PostMapping(ProviderConstant.PROVIDER_LOG_SET)
    @ApiOperation(value = "日志设置", notes = "日志设置")
    public ApiResponse<Boolean> set(CommonLog commonLog) {

        SysLog sysLog = new SysLog();
        BeanUtils.copyProperties(commonLog, sysLog);

        return ApiResponse.success(sysLogService.save(sysLog));
    }

}
