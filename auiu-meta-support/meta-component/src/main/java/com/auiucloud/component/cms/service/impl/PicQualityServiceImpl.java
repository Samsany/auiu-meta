package com.auiucloud.component.cms.service.impl;

import com.auiucloud.component.cms.mapper.PicQualityMapper;
import com.auiucloud.component.cms.vo.PicQualityVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.cms.domain.PicQuality;
import com.auiucloud.component.cms.service.IPicQualityService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* @author dries
* @description 针对表【cms_sd_up_scale(SD图片放大算法表)】的数据库操作Service实现
* @createDate 2023-05-21 20:04:17
*/
@Service
public class PicQualityServiceImpl extends ServiceImpl<PicQualityMapper, PicQuality>
    implements IPicQualityService {

    @Override
    public List<PicQualityVO> selectNormalPicQualityList() {
        return Optional.ofNullable(this.list(Wrappers.<PicQuality>lambdaQuery()
                        .eq(PicQuality::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(PicQuality::getSort)
                        .orderByDesc(PicQuality::getCreateTime)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::picQuality2VO)
                .toList();
    }

    private PicQualityVO picQuality2VO(PicQuality picQuality) {
        PicQualityVO picQualityVO = new PicQualityVO();
        BeanUtils.copyProperties(picQuality, picQualityVO);
        return picQualityVO;
    }
}




