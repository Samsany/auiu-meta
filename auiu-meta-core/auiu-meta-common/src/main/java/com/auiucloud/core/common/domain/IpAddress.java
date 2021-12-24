package com.auiucloud.core.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dries
 * @date 2021/12/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpAddress {

    private String regionNames;
    private String proCode;
    private String err;
    private String city;
    private String cityCode;
    private String ip;
    private String pro;
    private String regionCode;
    private String region;
    private String addr;

}
