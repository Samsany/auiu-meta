package com.auiucloud.component.sd.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SdConfigProperties implements Serializable {
    @Serial
    private static final long serialVersionUID = -5230461275983568252L;

    private String provider;

    private String name;

    private String code;

    private String url;

    private String appId;

    private String appSecret;

    private Integer isDefault;

    private String remark;
}
