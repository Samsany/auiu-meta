package com.auiucloud.gateway.model;

import lombok.Data;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.Serializable;
import java.util.List;

/**
 * 网关路由实例
 *
 * @author dries
 * @date 2021/12/27
 */
@Data
public class GatewayRoute implements Serializable {
    private static final long serialVersionUID = 7949469082270266974L;

    List<RouteDefinition> routes;

}
