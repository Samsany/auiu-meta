package com.auiucloud.uaa;

import com.auiucloud.core.feign.annotation.EnableMetaFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author dries
 */
@EnableMetaFeign
@SpringBootApplication
public class MetaUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaUaaApplication.class, args);
    }

}
