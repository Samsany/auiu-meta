package com.auiucloud.gen.datasource;

import com.auiucloud.gen.domain.SysDataSource;
import com.auiucloud.gen.utils.DataSourceUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据源工具类
 *
 * @author dries
 * @createDate 2022-08-18 11-58
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicDataSourceUtil {

    /**
     * 数据库连接池创建者
     */
    private final DefaultDataSourceCreator dataSourceCreator;
    /**
     * 动态路由数据连接
     */
    @Resource(type = DataSource.class)
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    /**
     * 获取数据源实体
     *
     * @param dataSource 数据源
     * @return 数据源配置
     */
    public DataSourceProperty setDataSourceProperty(SysDataSource dataSource) {

        String dsName = dataSource.getName();
        String username = dataSource.getUsername();
        String password = dataSource.getPassword();
        String params = dataSource.getJdbcParams();
        String dbType = dataSource.getDbType();
        // 组装连接的url
        String url = DataSourceUtil.DATA_SOURCE_URL(dbType, dataSource.getUrl(), dataSource.getPort(), dataSource.getDatabaseName(), params);

        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setPoolName(dsName);
        dataSourceProperty.setUrl(url);
        dataSourceProperty.setUsername(username);
        dataSourceProperty.setPassword(password);
        return dataSourceProperty;
    }

    /**
     * 获取数据源实体
     *
     * @param dsName   数据源名称
     * @param url      连接地址
     * @param username 用户名
     * @param password 密码
     * @return 数据源配置
     */
    public DataSourceProperty setDataSourceProperty(String dsName, String url, String username, String password) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setPoolName(dsName);
        dataSourceProperty.setUrl(url);
        dataSourceProperty.setUsername(username);
        dataSourceProperty.setPassword(password);
        return dataSourceProperty;
    }

    /**
     * 检查数据源配置是否可用
     *
     * @param dataSourceProperty 配置信息
     * @return boolean
     */
    public boolean isAvailableDataSourceProperty(DataSourceProperty dataSourceProperty) {
        try (Connection ignored = DriverManager.getConnection(dataSourceProperty.getUrl(),
                dataSourceProperty.getUsername(), dataSourceProperty.getPassword())) {
            if (log.isDebugEnabled()) {
                log.debug("check connection success, dataSourceProperty: {}", dataSourceProperty);
            }
        } catch (Exception e) {
            log.error("get connection error, dataSourceProperty: {}", dataSourceProperty, e);
            return false;
        }
        return true;
    }

    /**
     * 添加动态数据源
     *
     * @param dataSourceProperty 数据源配置
     */
    public void addDynamicDataSource(DataSourceProperty dataSourceProperty) {
        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        String dsName = dataSourceProperty.getPoolName();
        // 现阶段只支持MySQL
        // DbType dbType = DbType.MYSQL;
        dynamicRoutingDataSource.addDataSource(dsName, dataSource);
        log.debug("所有数据源列表：{}", dynamicRoutingDataSource.getDataSources());
    }

    /**
     * 移除动态数据源
     *
     * @param dsName 数据源名称
     */
    public void removeDynamicDataSource(String dsName) {
        dynamicRoutingDataSource.removeDataSource(dsName);
        log.debug("所有数据源列表：{}", dynamicRoutingDataSource.getDataSources());
    }

}
