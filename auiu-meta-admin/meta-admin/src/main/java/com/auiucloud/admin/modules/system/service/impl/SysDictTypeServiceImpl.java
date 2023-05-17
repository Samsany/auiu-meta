package com.auiucloud.admin.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.admin.modules.system.domain.SysDictType;
import com.auiucloud.admin.modules.system.mapper.SysDictTypeMapper;
import com.auiucloud.admin.modules.system.service.ISysDictDataService;
import com.auiucloud.admin.modules.system.service.ISysDictTypeService;
import com.auiucloud.admin.modules.system.vo.SysDictDataVO;
import com.auiucloud.admin.modules.system.vo.SysDictVO;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dries
 * @description 针对表【sys_dict_type(字典类型表)】的数据库操作Service实现
 * @createDate 2022-07-03 15:02:06
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    private final ISysDictDataService dictDataService;

    private static LambdaQueryWrapper<SysDictType> buildSearchParams(SysDictType dictType) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(dictType.getDictName())) {
            queryWrapper.like(SysDictType::getDictName, dictType.getDictName());
        }
        if (StrUtil.isNotBlank(dictType.getDictType())) {
            queryWrapper.eq(SysDictType::getDictType, dictType.getDictType());
        }
        if (ObjectUtil.isNotNull(dictType.getStatus())) {
            queryWrapper.eq(SysDictType::getStatus, dictType.getStatus());
        }

        queryWrapper.orderByDesc(SysDictType::getCreateTime);
        return queryWrapper;
    }

    /**
     * 查询字典类型列表
     *
     * @param dictType 查询参数
     * @return List<SysDictType>
     */
    @Override
    public List<SysDictVO> selectDictTypeList(SysDictType dictType) {
        List<SysDictVO> dictVOS = baseMapper.selectDictTypeList(dictType);
        Optional.ofNullable(dictVOS).orElse(Collections.emptyList())
                .parallelStream()
                .forEach(dictVO -> {
                    List<SysDictDataVO> dictDataVOS = Optional.ofNullable(dictVO.getDictDataList()).orElse(Collections.emptyList()).stream()
                            .sorted(Comparator.comparing(SysDictDataVO::getSort))
                            .collect(Collectors.toList());
                    dictVO.setDictDataList(dictDataVOS);
                });
        return dictVOS;
    }

    @Override
    public PageUtils listPage(Search search, SysDictType dictType) {
        LambdaQueryWrapper<SysDictType> queryWrapper = buildSearchParams(dictType);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    /**
     * 根据字典类型查询字典详情
     *
     * @param dictType 字典类型
     * @return SysDictVO
     */
    @Override
    public SysDictVO selectDictInfoByType(String dictType) {
        SysDictVO sysDictVO = baseMapper.selectDictInfoByType(dictType);
        List<SysDictDataVO> dictDataVOS = Optional.ofNullable(sysDictVO.getDictDataList()).orElse(Collections.emptyList()).stream()
                .sorted(Comparator.comparing(SysDictDataVO::getSort))
                .collect(Collectors.toList());
        sysDictVO.setDictDataList(dictDataVOS);
        return sysDictVO;
    }

    @Override
    public boolean checkDictTypeUnique(SysDictType dict) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getDictType, dict.getDictType());
        queryWrapper.ne(dict.getId() != null, SysDictType::getId, dict.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean checkDictNameUnique(SysDictType dict) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getDictName, dict.getDictName());
        queryWrapper.ne(dict.getId() != null, SysDictType::getId, dict.getId());
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




