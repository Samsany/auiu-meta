package com.auiucloud.core.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serial;
import java.io.Serializable;

/**
 * ws消息格式
 *
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WsMsgModel implements Serializable {

    @Serial
    private static final long serialVersionUID = -6300281648506773897L;

    /**
     * 消息编码
     */
    private String code;

    /**
     * 来自（保证唯一）
     */
    private String from;

    /**
     * 去自（保证唯一）
     */
    private String to;

    /**
     * 内容
     */
    private Object content;

}
