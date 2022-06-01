package com.auiucloud.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author dries
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.auiucloud")
public class AuiuMetaAdminApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(AuiuMetaAdminApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
