package com.auiucloud.admin;

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
public class AuiuMetaAdminApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(AuiuMetaAdminApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
