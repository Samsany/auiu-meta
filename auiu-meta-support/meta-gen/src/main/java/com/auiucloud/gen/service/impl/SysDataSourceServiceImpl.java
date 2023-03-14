package com.auiucloud.gen.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.gen.datasource.DynamicDataSourceUtil;
import com.auiucloud.gen.domain.SysDataSource;
import com.auiucloud.gen.dto.DataSourceConnectDTO;
import com.auiucloud.gen.mapper.SysDataSourceMapper;
import com.auiucloud.gen.service.ISysDataSourceService;
import com.auiucloud.gen.utils.DataSourceUtil;
import com.auiucloud.gen.vo.DataSourceVO;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        queryWrapper.orderByDesc(SysDataSource::getCreateTime);
        IPage<SysDataSource> page = this.page(PageUtils.getPage(search), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<DataSourceVO> dataSourceVOList = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList()).stream()
                .map(sysDataSource -> DataSourceVO.convertDataSource.apply(sysDataSource))
                .collect(Collectors.toList());
        pageUtils.setList(dataSourceVOList);
        return pageUtils;
    }

    @Override
    public SysDataSource getDataSourceByDsName(String dsName) {
        LambdaQueryWrapper<SysDataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDataSource::getName, dsName);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<SysDataSource> availableDataSourceList() {
        LambdaQueryWrapper<SysDataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDataSource::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        return Optional.ofNullable(this.list(queryWrapper)).orElse(Collections.emptyList());
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
    public boolean connectTest(DataSourceConnectDTO dataSource) {

        if (dataSource.getId() != null && StrUtil.isEmpty(dataSource.getPassword())) {
            SysDataSource sysDataSource = this.getById(dataSource.getId());
            dataSource.setPassword(sysDataSource.getPassword());
        }

        DataSourceProperty dataSourceProperty = dynamicDataSourceUtil.setDataSourceProperty(
                dataSource.getName(),
                DataSourceUtil.DATA_SOURCE_URL(
                        dataSource.getDbType(),
                        dataSource.getUrl(),
                        dataSource.getPort(),
                        dataSource.getDatabaseName(),
                        dataSource.getJdbcParams()),
                dataSource.getUsername(),
                dataSource.getPassword()
        );
        return dynamicDataSourceUtil.isAvailableDataSourceProperty(dataSourceProperty);
    }

    @Override
    public boolean addDataSource(SysDataSource dataSource) {
        if (this.checkDataSourceNameUnique(dataSource)) {
            throw new ApiException("新增数据源'" + dataSource.getName() + "'失败，数据源已存在");
        }
        if (this.connectTest(dataSource)) {
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
        if (this.connectTest(dataSource)) {
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

    private boolean connectTest(SysDataSource sysDataSource) {
        DataSourceConnectDTO connectDTO = new DataSourceConnectDTO();
        BeanUtils.copyProperties(sysDataSource, connectDTO);
        return !this.connectTest(connectDTO);
    }
}




