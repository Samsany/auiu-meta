package com.auiucloud.component.sd.domain;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDrawResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 5363547120422799952L;

    private List<String> images;

    private String info;

    private Object parameters;

}
