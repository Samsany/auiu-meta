package com.auiucloud.core.douyin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DouyinContentCheckDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2149178111169914627L;

    @NotBlank(message = "小程序标识不存在")
    private String appId;

    private String content;

    private List<String> contents;

    private String image;

    private String imageData;

}
