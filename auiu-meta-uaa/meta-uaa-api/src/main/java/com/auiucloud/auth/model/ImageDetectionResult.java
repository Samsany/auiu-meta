package com.auiucloud.auth.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class ImageDetectionResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -8651251633782467977L;

    //检测结果-状态码 0-成功 1-参数有误 2-access_token 校验失败 3-图片下载失败 4-服务内部错误
    private Integer error;

    //检测结果-消息
    private String message;
    // 检测结果-置信度-模型/标签
    private List<Predict> predicts;

    @Data
    public static class Predict {
        // 检测结果-置信度-模型/标签
        private String model_name;
        // 检测结果-置信度-结果，当值为 true 时表示检测的图片包含违法违规内容，比如是广告
        private Boolean hit;
    }

}
