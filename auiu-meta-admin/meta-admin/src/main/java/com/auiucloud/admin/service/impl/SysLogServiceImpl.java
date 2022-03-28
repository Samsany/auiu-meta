package com.auiucloud.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.admin.domain.SysLog;
import com.auiucloud.admin.mapper.SysLogMapper;
import com.auiucloud.admin.service.ISysLogService;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【sys_log(系统日志表)】的数据库操作Service实现
 * @createDate 2021-12-30 14:59:09
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public IPage<SysLog> listPage(Search search) {
        LambdaQueryWrapper<SysLog> queryWrapper = Wrappers.lambdaQuery();
        // 查询开始日期和结束日期
        queryWrapper.between(StrUtil.isNotBlank(search.getStartDate()), SysLog::getCreateTime, search.getStartDate(), search.getEndDate());
        // 关键词查询
        if (StrUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.and(i -> i.or().like(SysLog::getTitle, search.getKeyword()).or().like(SysLog::getTraceId, search.getKeyword()));
        }
        //　字段排序
        queryWrapper.orderByDesc(SysLog::getCreateTime);
        return this.baseMapper.selectPage(PageUtil.getPage(search), queryWrapper);
    }
}




