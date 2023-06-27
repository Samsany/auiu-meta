package com.auiucloud.component.sd.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dries
 **/
@Data
public class SdLoraVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8998280560613660059L;

    private Long id;

    private String title;

    private String value;

}
