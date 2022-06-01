package com.auiucloud.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author dries
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.auiucloud.**.feign.**")
public class AuiuMetaUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuiuMetaUaaApplication.class, args);
    }

}
