package com.auiucloud.gateway.handler;

import com.auiucloud.core.common.enums.EnvTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 网关默认首页
 *
 * @author DRIES
 */
@RestController
public class IndexHandler {

    @Value("${spring.profiles.active}")
    private String env;

    @GetMapping("/")
    public Mono<String> index() {
        return Mono.just(desc());
    }

    private String desc() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("<div style='color: blue'>MetaCloud gateway has been started!</div>");
        if (!EnvTypeEnum.PROD.getValue().equals(env)) {
            sb.append("<br/>");
            sb.append("<div><ul><li>文档地址：<a href='swagger-ui.html'>doc.html</a></li></ul></div>");
        }
        return sb.toString();
    }

}
