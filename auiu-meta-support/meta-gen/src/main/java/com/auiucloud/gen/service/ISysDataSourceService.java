package com.auiucloud.gen.service;

import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.gen.domain.SysDataSource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dries
 * @description 针对表【sys_data_source(系统数据源表)】的数据库操作Service
 * @createDate 2022-08-16 12:25:16
 */
public interface ISysDataSourceService extends IService<SysDataSource> {

    PageUtils listPage(Search search, SysDataSource dataSource);

    boolean checkDataSourceNameUnique(SysDataSource dataSource);
}
