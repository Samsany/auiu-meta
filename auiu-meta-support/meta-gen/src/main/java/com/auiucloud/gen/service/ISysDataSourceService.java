package com.auiucloud.gen.service;

import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.gen.domain.SysDataSource;
import com.auiucloud.gen.dto.DataSourceConnectDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_data_source(系统数据源表)】的数据库操作Service
 * @createDate 2022-08-16 12:25:16
 */
public interface ISysDataSourceService extends IService<SysDataSource> {

    /**
     * 数据源列表
     *
     * @param search     查询条件
     * @param dataSource 查询条件
     * @return PageUtils 分页对象
     */
    PageUtils listPage(Search search, SysDataSource dataSource);

    /**
     * 数据源连接测试
     *
     * @param dataSource 数据源
     * @return boolean
     */
    boolean connectTest(DataSourceConnectDTO dataSource);

    /**
     * 新增数据源
     *
     * @param dataSource 数据源
     * @return boolean
     */
    boolean addDataSource(SysDataSource dataSource);

    /**
     * 修改数据源
     *
     * @param dataSource 数据源
     * @return boolean
     */
    boolean updateDataSourceById(SysDataSource dataSource);

    /**
     * 删除数据源
     *
     * @param ids 数据源ID列表
     * @return boolean
     */
    boolean removeDataSourceByIds(List<Long> ids);

    /**
     * 校验数据源名称是否唯一
     *
     * @param dataSource 数据源
     * @return boolean
     */
    boolean checkDataSourceNameUnique(SysDataSource dataSource);

    /**
     * 启用的数据源列表
     *
     * @return
     */
    List<SysDataSource> availableDataSourceList();

    /**
     * 根据数据源名称查询数据源
     *
     * @return SysDataSource
     */
    SysDataSource getDataSourceByDsName(String dsName);
}
