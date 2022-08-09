package com.auiucloud.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.admin.domain.SysDictType;
import com.auiucloud.admin.mapper.SysDictTypeMapper;
import com.auiucloud.admin.service.ISysDictDataService;
import com.auiucloud.admin.service.ISysDictTypeService;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【sys_dict_type(字典类型表)】的数据库操作Service实现
 * @createDate 2022-07-03 15:02:06
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    private final ISysDictDataService dictDataService;

    @Override
    public PageUtils listPage(Search search, SysDictType dictType) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(search.getKeyword())) {
            queryWrapper.like(SysDictType::getDictName, search.getKeyword());
        }
        if (StrUtil.isNotBlank(dictType.getDictType())) {
            queryWrapper.eq(SysDictType::getDictType, dictType.getDictType());
        }
        if (ObjectUtil.isNotNull(search.getStatus())) {
            queryWrapper.eq(SysDictType::getStatus, search.getStatus());
        }

        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean checkDictTypeUnique(SysDictType dict) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getDictType, dict.getDictType());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean checkDictNameUnique(SysDictType dict) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getDictName, dict.getDictName());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public String removeDictTypeByIds(Long[] ids) {

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Long id : ids) {
            SysDictType dictType = this.getById(id);
            if (dictDataService.countDictDataByType(dictType.getDictType()) > 0) {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、字典 ").append(dictType.getDictName()).append(" 已分配，不能删除");
            } else {
                if (this.removeById(id)) {
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、字典 ").append(dictType.getDictName()).append(" 删除成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、字典 ").append(dictType.getDictName()).append(" 删除失败，请重试");
                }
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，数据批量删除失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ApiException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部删除成功！共 " + successNum + " 条，数据如下：");
        }

        return successMsg.toString();
    }

}




