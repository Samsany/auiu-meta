package com.auiucloud.core.common.constant;

import com.auiucloud.core.common.utils.StringPool;

/**
 * @author dries
 **/
public class MessageConstant {
    /**
     * 生产者服务名称
     */
    public static final String MESSAGE_PRODUCER_SERVICE = "meta-message-producer";
    /**
     * 消费者服务名称
     */
    public static final String MESSAGE_CONSUMER_SERVICE = "meta-message-consumer";
    /**
     * 生产者标识
     */
    public static final String OUTPUT = "out" + StringPool.DASH + StringPool.ZERO;
    /**
     * 消费者标识
     */
    public static final String INPUT = "in" + StringPool.DASH + StringPool.ZERO;

    /**
     * AI绘画消息(sd 文生图)
     */
    public static final String SD_TXT2IMG_MESSAGE = "txt2img" + StringPool.DASH;
    public static final String SD_TXT2IMG_DESTINATION = SD_TXT2IMG_MESSAGE + "topic";
    public static final String SD_TXT2IMG_BINDER_GROUP = SD_TXT2IMG_MESSAGE + "binder-group";
    public static final String SD_TXT2IMG_MESSAGE_OUTPUT = SD_TXT2IMG_MESSAGE + OUTPUT;
    public static final String SD_TXT2IMG_MESSAGE_INPUT = SD_TXT2IMG_MESSAGE + INPUT;
    public static final String SD_TXT2IMG_MESSAGE_QUEUE = SD_TXT2IMG_DESTINATION + StringPool.DOT + SD_TXT2IMG_BINDER_GROUP;
    public static final String SD_TXT2IMG_MESSAGE_DLQ_QUEUE = SD_TXT2IMG_MESSAGE_QUEUE + StringPool.DOT + "dlq";

}
