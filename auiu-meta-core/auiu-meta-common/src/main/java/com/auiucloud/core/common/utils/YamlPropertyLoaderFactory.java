package com.auiucloud.core.common.utils;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.List;

/**
 * 加载yml格式的自定义配置文件
 *
 * @author dries
 * @date 2021/12/21
 */
public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        List<PropertySource<?>> sources = new YamlPropertySourceLoader()
                .load(
                        resource.getResource().getFilename(),
                        resource.getResource()
                );
        if (sources.size() == 0) {
            return super.createPropertySource(name, resource);
        }
        return sources.get(0);
    }
}
