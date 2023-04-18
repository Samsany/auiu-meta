package com.auiucloud.component.cms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.component.cms.domain.PicTag;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.model.dto.UpdateStatusDTO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.component.cms.domain.SwiperAdv;
import com.auiucloud.component.cms.service.ISwiperAdvService;
import com.auiucloud.component.cms.mapper.SwiperAdvMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author dries
* @description 针对表【cms_swiper_adv(轮播广告表)】的数据库操作Service实现
* @createDate 2023-04-11 16:03:44
*/
@Service
public class SwiperAdvServiceImpl extends ServiceImpl<SwiperAdvMapper, SwiperAdv>
    implements ISwiperAdvService {

    @Override
    public PageUtils listPage(Search search, SwiperAdv swiperAdv) {
        LambdaQueryWrapper<SwiperAdv> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(search.getKeyword()), SwiperAdv::getName, search.getKeyword());
        queryWrapper.orderByDesc(SwiperAdv::getSort);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public List<SwiperAdv> selectCommonSwiperAdvList(SwiperAdv swiperAdv) {
        LambdaQueryWrapper<SwiperAdv> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SwiperAdv::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.eq(SwiperAdv::getType, swiperAdv.getType());
        queryWrapper.orderByDesc(SwiperAdv::getSort);
        queryWrapper.orderByDesc(SwiperAdv::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public boolean setStatus(UpdateStatusDTO updateStatusDTO) {
        LambdaUpdateWrapper<SwiperAdv> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SwiperAdv::getStatus, updateStatusDTO.getStatus());
        wrapper.eq(SwiperAdv::getId, updateStatusDTO.getId());
        return this.update(wrapper);
    }
}




