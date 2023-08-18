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
public class WsMsgModel<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -6300281648506773897L;

    /**
     * 消息编码
     */
    private String code;

    /**
     * 发送类型:
     *
     * 0-发送给所有人
     * 1-指定用户发送
     * 2-指定部分用户发送
     * 3-排除部分用户发送
     */
    private Integer sendType;

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
    private T content;

}
