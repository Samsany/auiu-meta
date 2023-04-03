package com.auiucloud.ums;

import com.auiucloud.core.feign.annotation.EnableMetaFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableMetaFeign
@SpringBootApplication
@EnableTransactionManagement
public class MetaUmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaUmsApplication.class, args);
    }

}
