package com.auiucloud.auth.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class DouyinTextAntidirtResult implements Serializable {

    @Serial
    private static final long serialVersionUID = -2099668243224129466L;

    // 请求 id
    private String log_id;

    private List<ResultData> data;

    @Data
    public static class ResultData {
        // 检测结果-消息
        private String msg;
        // 检测结果-状态码 0-成功 400-参数有误 401-access_token 校验失败
        private Integer code;
        // 检测结果-任务 id
        private String task_id;
        // 检测结果-置信度列表
        private List<Predicts> predicts;
        // 检测结果-数据 id
        private String data_id;
    }

    @Data
    public static class Predicts {
        // 检测结果-置信度-概率，仅供参考，可以忽略
        private Integer prob;
        // 检测结果-置信度-结果，当值为 true 时表示检测的文本包含违法违规内容
        private Boolean hit;
        // 检测结果-置信度-服务/目标
        private String target;
        // 检测结果-置信度-模型/标签
        private String model_name;
    }
}
