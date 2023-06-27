package com.auiucloud.component.cms.dto;

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
public class UpdateAiDrawPicDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6949990793951060668L;

    private Long id;

    private String pic;
}
