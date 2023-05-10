package com.auiucloud.auth;

import com.auiucloud.core.feign.annotation.EnableMetaFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author dries
 */
@EnableMetaFeign
@SpringBootApplication
@EnableTransactionManagement
public class MetaAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(MetaAuthApplication.class, args);
    }

}
