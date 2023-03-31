package com.auiucloud.component;

import com.auiucloud.core.feign.annotation.EnableMetaFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableMetaFeign
@SpringBootApplication
public class MetaComponentApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaComponentApplication.class, args);
    }

}
