package com.auiucloud.ums.domain;

import com.auiucloud.core.database.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 用户意见反馈表
 *
 * @TableName ums_user_feedback
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value = "ums_user_feedback")
public class UserFeedback extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -4692895124796384906L;

    /**
     * 用户ID
     */
    private Long uId;

    /**
     * 联系方式
     */
    private String contactInformation;

    /**
     * 反馈类型
     */
    private String feedType;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 状态(0-已提交 1-已回复)
     */
    private Integer status;

    /**
     * 图片地址列表
     */
    private String picUrls;

}
