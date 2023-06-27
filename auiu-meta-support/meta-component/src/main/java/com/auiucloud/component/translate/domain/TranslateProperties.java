package com.auiucloud.component.translate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslateProperties {

    private String provider;

    private String name;

    private String code;

    private String appId;

    private String appSecret;

    private Integer isDefault;

    private String remark;

}
