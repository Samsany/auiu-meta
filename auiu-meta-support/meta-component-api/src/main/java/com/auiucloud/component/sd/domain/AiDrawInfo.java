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
public class AiDrawInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 8130025503973088249L;

    private String taskId;

    private Long galleryId;

    private String imageData;

    private String url;

    private Integer status;

}
