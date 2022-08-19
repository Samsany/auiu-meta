package com.auiucloud.gen.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.gen.datasource.DynamicDataSourceUtil;
import com.auiucloud.gen.domain.SysDataSource;
import com.auiucloud.gen.mapper.SysDataSourceMapper;
import com.auiucloud.gen.service.ISysDataSourceService;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_data_source(系统数据源表)】的数据库操作Service实现
 * @createDate 2022-08-16 12:25:16
 */
@Service
@RequiredArgsConstructor
public class SysDataSourceServiceImpl extends ServiceImpl<SysDataSourceMapper, SysDataSource> implements ISysDataSourceService {

    private final DynamicDataSourceUtil dynamicDataSourceUtil;

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

    @Override
    public boolean connectTest(SysDataSource dataSource) {
        DataSourceProperty dataSourceProperty = dynamicDataSourceUtil.setDataSourceProperty(dataSource);
        return dynamicDataSourceUtil.isAvailableDataSourceProperty(dataSourceProperty);
    }

    @Override
    public boolean addDataSource(SysDataSource dataSource) {
        if (this.checkDataSourceNameUnique(dataSource)) {
            throw new ApiException("新增数据源'" + dataSource.getName() + "'失败，数据源已存在");
        }
        if (!this.connectTest(dataSource)) {
            throw new ApiException("数据源'" + dataSource.getName() + "'连接失败，请检查配置");
        }
        boolean result = this.save(dataSource);
        if (result) {
            DataSourceProperty dataSourceProperty = dynamicDataSourceUtil.setDataSourceProperty(dataSource);
            dynamicDataSourceUtil.addDynamicDataSource(dataSourceProperty);
        }
        return result;
    }

    @Override
    public boolean updateDataSourceById(SysDataSource dataSource) {
        if (this.checkDataSourceNameUnique(dataSource)) {
            throw new ApiException("修改数据源'" + dataSource.getName() + "'失败，数据源已存在");
        }
        if (!this.connectTest(dataSource)) {
            throw new ApiException("数据源'" + dataSource.getName() + "'连接失败，请检查配置");
        }
        boolean result = this.updateById(dataSource);
        if (result) {
            // 修改成功，先移除该数据源
            dynamicDataSourceUtil.removeDynamicDataSource(dataSource.getName());
            // 重新添加数据源
            DataSourceProperty dataSourceProperty = dynamicDataSourceUtil.setDataSourceProperty(dataSource);
            dynamicDataSourceUtil.addDynamicDataSource(dataSourceProperty);
        }
        return result;
    }

    @Override
    public boolean removeDataSourceByIds(List<Long> ids) {
        List<SysDataSource> dataSourceList = this.listByIds(ids);
        boolean result = this.removeByIds(ids);
        if (result) {
            for (SysDataSource dataSource : dataSourceList) {
                dynamicDataSourceUtil.removeDynamicDataSource(dataSource.getName());
            }
        }
        return result;
    }
}




