package com.auiucloud.component.sd.vo;

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
public class SdWaitQueueVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6777529653803082133L;

    /**
     * 队列等待数量
     */
    private Integer queueMessageCount;

    /**
     * 队列变动类型
     */
    private Integer changeType;
}
