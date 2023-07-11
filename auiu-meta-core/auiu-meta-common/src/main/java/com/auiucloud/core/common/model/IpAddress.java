package com.auiucloud.core.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 * @date 2021/12/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IpAddress implements Serializable {

    @Serial
    private static final long serialVersionUID = 7646747214997153325L;

    private String regionNames;
    private String proCode;
    private String err;
    private String city;
    private String cityCode;
    private String ip = "127.0.0.1";
    private String pro;
    private String regionCode;
    private String region = "localhost";
    private String addr;

}
