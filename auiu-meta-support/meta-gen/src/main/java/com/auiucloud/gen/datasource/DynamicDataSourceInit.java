package com.auiucloud.gen.datasource;

import com.auiucloud.gen.domain.SysDataSource;
import com.auiucloud.gen.service.ISysDataSourceService;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据源初始化加载
 *
 * @author dries
 * @createDate 2022-08-18 11-56
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicDataSourceInit implements InitializingBean {

    private final ISysDataSourceService sysDataSourceService;
    private final DynamicDataSourceUtil dynamicDataSourceUtil;

    @Override
    public void afterPropertiesSet() {
        // 查询已启用的数据源列表
        List<SysDataSource> list = sysDataSourceService.availableDataSourceList();

        for (SysDataSource dataSource : list) {
            DataSourceProperty dataSourceProperty = dynamicDataSourceUtil.setDataSourceProperty(dataSource);
            log.debug("数据源是否可用：{}", dynamicDataSourceUtil.isAvailableDataSourceProperty(dataSourceProperty));
            if (dynamicDataSourceUtil.isAvailableDataSourceProperty(dataSourceProperty)) {
                dynamicDataSourceUtil.addDynamicDataSource(dataSourceProperty);
                log.info("数据源动态加载完成：{}", dataSource.getName());
            }

        }
    }
}
