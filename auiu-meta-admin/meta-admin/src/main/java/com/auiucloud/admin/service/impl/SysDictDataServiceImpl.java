package com.auiucloud.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.admin.domain.SysDictData;
import com.auiucloud.admin.mapper.SysDictDataMapper;
import com.auiucloud.admin.service.ISysDictDataService;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dries
 * @description 针对表【sys_dict_data(字典数据表)】的数据库操作Service实现
 * @createDate 2022-07-03 15:02:06
 */
@Service
@RequiredArgsConstructor
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData>
        implements ISysDictDataService {

    @Override
    public PageUtils listPage(Search search, SysDictData dictData) {
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(dictData.getDictLabel())) {
            queryWrapper.like(SysDictData::getDictLabel, search.getKeyword());
        }
        if (StrUtil.isNotBlank(dictData.getDictType())) {
            queryWrapper.eq(SysDictData::getDictType, dictData.getDictType());
        }
        if (ObjectUtil.isNotNull(search.getStatus())) {
            queryWrapper.eq(SysDictData::getStatus, search.getStatus());
        }

        queryWrapper.orderByDesc(SysDictData::getSort);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public long countDictDataByType(String dictType) {
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictData::getDictType, dictType);
        return this.count(queryWrapper);
    }

    @Transactional
    @Override
    public boolean addSysDictData(SysDictData dictData) {
        boolean result = this.save(dictData);
        if (result && dictData.getIsDefault().equals(CommonConstant.STATUS_DISABLE_VALUE))
            batchSetDictDataDefault(dictData);
        return result;
    }

    /**
     * 编辑字典项
     *
     * @param dictData 字典项
     * @return boolean
     */
    @Transactional
    @Override
    public boolean editSysDictData(SysDictData dictData) {
        boolean result = this.updateById(dictData);
        if (result && dictData.getIsDefault().equals(CommonConstant.STATUS_DISABLE_VALUE))
            batchSetDictDataDefault(dictData);
        return result;
    }

    private void batchSetDictDataDefault(SysDictData dictData) {
        LambdaUpdateWrapper<SysDictData> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysDictData::getIsDefault, 0);
        updateWrapper.eq(SysDictData::getDictType, dictData.getDictType());
        updateWrapper.ne(SysDictData::getId, dictData.getId());
        this.update(updateWrapper);
    }
}




