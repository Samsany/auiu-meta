package com.auiucloud.component.sd.vo;

import com.auiucloud.component.sd.domain.SdTxt2ImgParams;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author dries
 **/
@Data
public class SdDrawSaveVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6863233932055643826L;

    @NotEmpty(message = "参数异常")
    private List<String> images;

    @NotNull(message = "参数异常")
    private SdTxt2ImgParams sdTxt2ImgParams;

}
