package com.auiucloud.core.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author dries
 * @date 2022/3/1
 */
@Configuration
public class AuthenticationBeanConfig {

    /**
     * 默认密码处理器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        //        // 默认编码算法的 Id
        //        String idForEncode = "bcrypt";
        //        // 多种密码编码共存
        //        Map<String, PasswordEncoder> encoders = new HashMap<>();
        //        encoders.put(idForEncode, new BCryptPasswordEncoder());
        //        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        //        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        //        encoders.put("scrypt", new SCryptPasswordEncoder());
        //        encoders.put("sha256", new StandardPasswordEncoder());
        //        return new DelegatingPasswordEncoder(idForEncode, encoders);
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
