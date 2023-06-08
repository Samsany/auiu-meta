package com.auiucloud.core.rocketmq.model;

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
public class UserPointChangeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4127401586928547728L;

    private Long userId;

    private Integer point;

    private Integer type;

    private String note;

    private Integer addAndSubtract;

    private Integer changeType;

}
