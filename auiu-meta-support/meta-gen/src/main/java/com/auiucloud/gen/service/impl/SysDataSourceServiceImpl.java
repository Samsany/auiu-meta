package com.auiucloud.gen.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.gen.domain.SysDataSource;
import com.auiucloud.gen.mapper.SysDataSourceMapper;
import com.auiucloud.gen.service.ISysDataSourceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【sys_data_source(系统数据源表)】的数据库操作Service实现
 * @createDate 2022-08-16 12:25:16
 */
@Service
public class SysDataSourceServiceImpl extends ServiceImpl<SysDataSourceMapper, SysDataSource> implements ISysDataSourceService {

    @Override
    public PageUtils listPage(Search search, SysDataSource dataSource) {
        LambdaQueryWrapper<SysDataSource> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.like(SysDataSource::getName, search.getKeyword());
        }

        if (ObjectUtil.isNotNull(search.getStatus())) {
            queryWrapper.eq(SysDataSource::getStatus, search.getStatus());
        }

        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean checkDataSourceNameUnique(SysDataSource dataSource) {
        LambdaQueryWrapper<SysDataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDataSource::getName, dataSource.getName());
        queryWrapper.ne(dataSource.getId() != null, SysDataSource::getId, dataSource.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }
}




