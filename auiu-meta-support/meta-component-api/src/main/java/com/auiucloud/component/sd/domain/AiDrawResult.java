package com.auiucloud.component.sd.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private static final long serialVersionUID = -5075419407290210319L;

    private String taskId;

    private String drawIds;

    private List<AiDrawInfo> aiDrawList;

}
