package com.auiucloud.component.cms.service.impl;

import com.auiucloud.component.cms.domain.PicRatio;
import com.auiucloud.component.cms.mapper.PicRatioMapper;
import com.auiucloud.component.cms.service.IPicRatioService;
import com.auiucloud.component.cms.vo.PicRatioVO;
import com.auiucloud.core.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【cms_pic_ratio(图片比例表)】的数据库操作Service实现
 * @createDate 2023-05-21 14:52:48
 */
@Service
public class PicRatioServiceImpl extends ServiceImpl<PicRatioMapper, PicRatio>
        implements IPicRatioService {

    @Override
    public List<PicRatioVO> selectNormalPicRatioVOList() {
        return Optional.ofNullable(this.list(Wrappers.<PicRatio>lambdaQuery()
                        .eq(PicRatio::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                        .orderByDesc(PicRatio::getSort)
                        .orderByDesc(PicRatio::getCreateTime)
                ))
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(this::picRatio2VO)
                .toList();
    }

    private PicRatioVO picRatio2VO(PicRatio picRatio) {
        PicRatioVO picRatioVO = new PicRatioVO();
        BeanUtils.copyProperties(picRatio, picRatioVO);
        return picRatioVO;
    }
}




