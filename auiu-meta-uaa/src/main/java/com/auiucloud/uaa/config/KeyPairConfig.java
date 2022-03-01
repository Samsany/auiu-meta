package com.auiucloud.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * @author dries
 * @date 2022/2/9
 */
@Configuration
public class KeyPairConfig {

    @Bean
    public KeyPair keyPair() {
        ClassPathResource ksFile = new ClassPathResource("meta-cloud.jks");
        KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(ksFile, "Zxx131013@".toCharArray());
        return ksFactory.getKeyPair("meta-cloud");
    }

}
