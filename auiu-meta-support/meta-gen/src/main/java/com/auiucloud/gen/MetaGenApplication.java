package com.auiucloud.gen;

import com.auiucloud.core.feign.annotation.EnableMetaFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableMetaFeign
@EnableDiscoveryClient
@SpringBootApplication
public class MetaGenApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaGenApplication.class, args);
    }

}
