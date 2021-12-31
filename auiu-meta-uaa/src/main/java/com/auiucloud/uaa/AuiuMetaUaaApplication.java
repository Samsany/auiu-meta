package com.auiucloud.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dries
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AuiuMetaUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuiuMetaUaaApplication.class, args);
    }

}
