package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysDictType;
import com.auiucloud.admin.vo.SysDictVO;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_dict_type(字典类型表)】的数据库操作Service
 * @createDate 2022-07-03 15:02:06
 */
public interface ISysDictTypeService extends IService<SysDictType> {

    /**
     * 查询字典类型列表
     *
     * @param dictType 查询参数
     * @return List<SysDictType>
     */
    List<SysDictVO> selectDictTypeList(SysDictType dictType);

    /**
     * 分页查询字典类型列表
     *
     * @param search 查询参数
     * @param dictType 查询参数
     * @return PageUtils
     */
    PageUtils listPage(Search search, SysDictType dictType);

    /**
     * 根据字典类型查询字典详情
     *
     * @param dictType 字典类型
     * @return SysDictVO
     */
    SysDictVO selectDictInfoByType(String dictType);

    /**
     * 校验字典类型是否唯一
     *
     * @param dict 字典信息
     * @return boolean
     */
    boolean checkDictTypeUnique(SysDictType dict);

    /**
     * 校验字典名称是否唯一
     *
     * @param dict 字典信息
     * @return boolean
     */
    boolean checkDictNameUnique(SysDictType dict);

    /**
     * 批量删除字典类型
     *
     * @param ids
     * @return
     */
    String removeDictTypeByIds(Long[] ids);
}
