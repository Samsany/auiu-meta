package com.auiucloud.component.sd.service;

import com.auiucloud.component.sd.domain.SdDrawRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【sd_draw_record(SD绘画记录表)】的数据库操作Service
* @createDate 2023-06-21 13:42:19
*/
public interface ISdDrawRecordService extends IService<SdDrawRecord> {

    SdDrawRecord getSdDrawRecordByUId2Id(Long userId, String taskId);

}
