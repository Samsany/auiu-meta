package com.auiucloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dries
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AuiuMetaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuiuMetaGatewayApplication.class, args);
    }

}
