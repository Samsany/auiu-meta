package com.auiucloud.admin;

import com.auiucloud.core.feign.annotation.EnableMetaFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author dries
 */
//@EnableMetaXxlJob
@EnableMetaFeign
@SpringBootApplication
public class MetaAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaAdminApplication.class, args);
    }

}
