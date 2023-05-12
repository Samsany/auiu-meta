package com.auiucloud.ums.dto;

import com.auiucloud.ums.enums.UserTaskEnums;
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
public class UserTaskAwardDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3782162994937095426L;

    private Long userId;

    private Integer point;

    private UserTaskEnums.UserTaskTagEnum tagEnum;
}
