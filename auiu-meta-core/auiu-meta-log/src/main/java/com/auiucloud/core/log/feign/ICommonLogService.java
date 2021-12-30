package com.auiucloud.core.log.feign;

import com.auiucloud.core.common.api.ApiResponse;
import com.auiucloud.core.common.model.CommonLog;
import com.auiucloud.core.feign.constant.FeignConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 公共日志
 *
 * @author dries
 * @date 2021/12/24
 */
@FeignClient(value = FeignConstant.AUIU_MATE_CLOUD_LOG_PRODUCER)
public interface ICommonLogService {

    /**
     * 向消息中心发送消息
     *
     * @param commonLog 普通日志
     * @return 状态
     */
    @PostMapping("/provider/common-log/send")
    ApiResponse<?> sendCommonLog(CommonLog commonLog);

}
