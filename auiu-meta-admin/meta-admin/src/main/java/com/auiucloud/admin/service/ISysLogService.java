package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysLog;
import com.auiucloud.core.database.model.Search;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【sys_log(系统日志表)】的数据库操作Service
* @createDate 2021-12-30 14:59:09
*/
public interface ISysLogService extends IService<SysLog> {

    /**
     * 日志分页列表
     *
     * @param search 搜索和分页对象
     * @return 日志分页列表
     */
    IPage<SysLog> listPage(Search search);

}
