package com.auiucloud.component.sd.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.auiucloud.core.common.constant.RedisKeyConstant;
import com.auiucloud.core.redis.core.RedisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.sd.domain.SdDrawRecord;
import com.auiucloud.component.sd.service.ISdDrawRecordService;
import com.auiucloud.component.sd.mapper.SdDrawRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author dries
* @description 针对表【sd_draw_record(SD绘画记录表)】的数据库操作Service实现
* @createDate 2023-06-21 13:42:19
*/
@Service
@RequiredArgsConstructor
public class SdDrawRecordServiceImpl extends ServiceImpl<SdDrawRecordMapper, SdDrawRecord>
    implements ISdDrawRecordService {

    private final RedisService redisService;
    @Override
    public SdDrawRecord getSdDrawRecordByUId2Id(Long userId, String taskId) {
        SdDrawRecord sdDrawRecord = (SdDrawRecord) redisService.get(RedisKeyConstant.cacheSdDrawKey(String.valueOf(userId), String.valueOf(taskId)));
        if (ObjectUtil.isNotNull(sdDrawRecord)) {
            sdDrawRecord = this.getById(taskId);
            redisService.set(RedisKeyConstant.cacheSdDrawKey(String.valueOf(userId), String.valueOf(taskId)), sdDrawRecord);
        }
        return sdDrawRecord;
    }
}




