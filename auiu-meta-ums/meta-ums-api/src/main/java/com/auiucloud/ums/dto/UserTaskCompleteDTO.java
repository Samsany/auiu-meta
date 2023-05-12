package com.auiucloud.ums.dto;

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
public class UserTaskCompleteDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -11536121770709460L;

    private Long uId;

    private Long taskId;

    private String taskTag;
}
