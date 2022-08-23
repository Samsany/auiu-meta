package com.auiucloud.gen.service.impl;

import com.auiucloud.gen.domain.GenTable;
import com.auiucloud.gen.mapper.GenTableMapper;
import com.auiucloud.gen.service.IGenTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【gen_table(代码生成业务表)】的数据库操作Service实现
 * @createDate 2022-08-15 23:44:26
 */
@Service
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements IGenTableService {

    // @Override
    // public List<GenTable> selectDbTableListByDsName(Long dataSourceId) {
    //     SysDataSource sysDataSource = sysDataSourceService.getById(dataSourceId);
    //     GlobalConfig gc = new GlobalConfig();
    //
    //     DataSourceConfig dsc = new DataSourceConfig();
    //     dsc.setDbType(DbType.getDbType(sysDataSource.getDbType()));
    //     dsc.setDriverName(sysDataSource.getDriverClass());
    //     dsc.setUrl(sysDataSource.getUrl());
    //     dsc.setUsername(sysDataSource.getUsername());
    //     dsc.setPassword(sysDataSource.getPassword());
    //
    //     StrategyConfig strategyConfig = new StrategyConfig();
    //     TemplateConfig templateConfig = new TemplateConfig();
    //     ConfigBuilder config = new ConfigBuilder(new PackageConfig(), dsc, strategyConfig, templateConfig, gc);
    //     List<TableInfo> list = config.getTableInfoList();
    //     // 查询出
    //     return null;
    // }

}




