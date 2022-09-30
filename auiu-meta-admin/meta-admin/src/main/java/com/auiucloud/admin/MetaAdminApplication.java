package com.auiucloud.admin;

import com.auiucloud.core.feign.annotation.EnableMetaFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author dries
 */
@EnableMetaFeign
@SpringBootApplication
public class MetaAdminApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(MetaAdminApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
