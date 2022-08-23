package com.auiucloud.gen.service.impl;

import com.auiucloud.gen.domain.GenTable;
import com.auiucloud.gen.domain.SysDataSource;
import com.auiucloud.gen.mapper.GenTableMapper;
import com.auiucloud.gen.service.IGenTableService;
import com.auiucloud.gen.service.ISysDataSourceService;
import com.auiucloud.gen.utils.DataSourceUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dries
 * @description 针对表【gen_table(代码生成业务表)】的数据库操作Service实现
 * @createDate 2022-08-15 23:44:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements IGenTableService {

    private final ISysDataSourceService dataSourceService;

    @Override
    public List<TableInfo> selectDbTableListById(Long dataSourceId) {
        SysDataSource dataSource = dataSourceService.getById(dataSourceId);

        // 数据源配置
        // DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig
        //          .Builder("jdbc:oracle:thin:@172.18.2.10:1521:stdb", "ecology", "QJ9svVFK18mPH6rw")
        //          .typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
        //              if (metaInfo.getJdbcType().equals(JdbcType.NUMERIC) && metaInfo.getScale() == 0) {
        //                  return DbColumnType.LONG;
        //              } else {
        //                  return typeRegistry.getColumnType(metaInfo);
        //              }
        //          })
        //          .build();
        //  AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        //  List<TableInfo> tableInfoList = generator.getConfig().getTableInfoList();

        // GlobalConfig gc = new GlobalConfig.Builder()
        //         .build();
        // PackageConfig pc = new PackageConfig.Builder()
        //         .build();
        // TemplateConfig tc = new TemplateConfig.Builder()
        //         .build();
        // StrategyConfig sc = new StrategyConfig.Builder()
        //         .build();
        // InjectionConfig ic = new InjectionConfig.Builder()
        //         .build();

        DataSourceConfig dsc = new DataSourceConfig.Builder(
                DataSourceUtil.DATA_SOURCE_URL(
                        dataSource.getDbType(),
                        dataSource.getUrl(),
                        dataSource.getPort(),
                        dataSource.getDatabaseName(),
                        dataSource.getJdbcParams()
                ),
                dataSource.getUsername(),
                dataSource.getPassword()
        ).build();

        ConfigBuilder configBuilder = new ConfigBuilder(null, dsc, null, null, null, null);
        return configBuilder.getTableInfoList();
    }

}




