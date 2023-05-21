package com.auiucloud.core.web.config;

import com.auiucloud.core.web.props.HttpClientProperties;
import com.auiucloud.core.web.utils.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author dries
 * @date 2021/12/22
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(HttpClientProperties.class)
public class WebConfiguration implements WebMvcConfigurer {

    @Resource
    private HttpClientProperties httpClientProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public RestTemplate restTemplate() {
        // 超时处理设置
        // 应用超时设置
        return new RestTemplate(httpRequestFactory());
    }

    @Bean
    public RestTemplateUtil restTemplateUtil() {
        return new RestTemplateUtil();
    }


    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }


    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        // 设置整个连接池最大连接数
        connectionManager.setMaxTotal(httpClientProperties.getMaxTotal());

        // 路由是对maxTotal的细分
        connectionManager.setDefaultMaxPerRoute(httpClientProperties.getMaxPerRoute());
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(httpClientProperties.getSocketTimeout())  //返回数据的超时时间
                .setConnectTimeout(httpClientProperties.getConnectTimeout()) //连接上服务器的超时时间
                .setConnectionRequestTimeout(httpClientProperties.getConnectRequestTimeout()) //从连接池中获取连接的超时时间
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }

}
