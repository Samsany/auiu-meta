package com.auiucloud.gen.config;

import com.auiucloud.gen.datasource.MasterDataSourceProvider;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dries
 * @createDate 2022-08-18 11-47
 */
@Configuration
public class DynamicDataSourceConfig {

    @Bean
    public MasterDataSourceProvider masterDataSourceProvider(DataSourceProperties dataSourceProperties, DefaultDataSourceCreator defaultDataSourceCreator) {
        return new MasterDataSourceProvider(dataSourceProperties, defaultDataSourceCreator);
    }

}
