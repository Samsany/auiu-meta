package com.auiucloud.core.web.config;

import com.auiucloud.core.web.datatype.DateTimeDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigInteger;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author dries
 * @createDate 2022-07-03 12-05
 */
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class DateConverterConfiguration {

    @Bean
    @Primary
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = null;
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .locale(Locale.CHINA)
                .timeZone(TimeZone.getTimeZone("GMT+8"))
                .modules(new DateTimeDeserializer())
                // 全局转化Long类型为String，解决序列化后传入前端Long类型精度丢失问题
                .serializerByType(BigInteger.class, ToStringSerializer.instance)
                .serializerByType(Long.class, ToStringSerializer.instance)
                .build();
        return objectMapper;
    }

}
