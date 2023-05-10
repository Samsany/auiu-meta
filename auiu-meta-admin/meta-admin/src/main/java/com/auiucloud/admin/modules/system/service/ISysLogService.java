package com.auiucloud.admin.modules.system.service;

import com.auiucloud.admin.modules.system.domain.SysLog;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_log(系统日志表)】的数据库操作Service
 * @createDate 2022-05-31 14:59:09
 */
public interface ISysLogService extends IService<SysLog> {

    /**
     * 查询系统日志分页列表
     *
     * @param search 搜索参数
     * @param sysLog 系统日志
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysLog sysLog);

    /**
     * 查询系统日志列表
     *
     * @param search 搜索参数
     * @param sysLog 系统日志
     * @return List<SysLog>
     */
    List<SysLog> selectSysLogList(Search search, SysLog sysLog);

}
