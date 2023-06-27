package com.auiucloud.admin.modules.system.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.auiucloud.admin.modules.system.domain.SysLog;
import com.auiucloud.admin.modules.system.mapper.SysLogMapper;
import com.auiucloud.admin.modules.system.service.ISysLogService;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_log(系统日志表)】的数据库操作Service实现
 * @createDate 2022-05-31 14:59:09
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {


    @Override
    public PageUtils listPage(Search search, SysLog sysLog) {
        return new PageUtils(this.page(PageUtils.getPage(search), buildSysLogSearch(search, sysLog)));
    }

    @Override
    public List<SysLog> selectSysLogList(Search search, SysLog sysLog) {
        return this.list(buildSysLogSearch(search, sysLog));
    }

    /**
     * 组装查询参数
     *
     * @param search 查询参数
     * @param sysLog 系统日志
     * @return LambdaQueryWrapper<SysLog>
     */
    private LambdaQueryWrapper<SysLog> buildSysLogSearch(Search search, SysLog sysLog) {
        LambdaQueryWrapper<SysLog> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank(search.getStartDate())) {
            queryWrapper.between(SysLog::getCreateTime, search.getStartDate(), search.getEndDate());
        }
        if (StringUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.like(SysLog::getId, search.getKeyword());
        }
        if (StringUtil.isNotBlank(sysLog.getTitle())) {
            queryWrapper.eq(SysLog::getTitle, sysLog.getTitle());
        }
        if (StringUtil.isNotBlank(sysLog.getMethod())) {
            queryWrapper.eq(SysLog::getMethod, sysLog.getMethod());
        }
        if (StringUtil.isNotBlank(sysLog.getUrl())) {
            queryWrapper.eq(SysLog::getUrl, sysLog.getUrl());
        }
        queryWrapper.orderByDesc(SysLog::getCreateTime);
        queryWrapper.orderByDesc(SysLog::getId);
        return queryWrapper;
    }
}




