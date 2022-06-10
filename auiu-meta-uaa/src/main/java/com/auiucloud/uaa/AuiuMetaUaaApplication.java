package com.auiucloud.uaa;

import com.auiucloud.core.feign.annotation.EnableMetaFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dries
 */
@EnableMetaFeign
@EnableDiscoveryClient
@SpringBootApplication
public class AuiuMetaUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuiuMetaUaaApplication.class, args);
    }

}
