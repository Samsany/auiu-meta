package com.auiucloud.admin.modules.system.mapper;

import com.auiucloud.admin.modules.system.domain.SysDictType;
import com.auiucloud.admin.modules.system.vo.SysDictVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_dict_type(字典类型表)】的数据库操作Mapper
 * @createDate 2022-07-03 15:02:06
 * @Entity com.auiucloud.admin.modules.system.domain.SysDictType
 */
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

    List<SysDictVO> selectDictTypeList(SysDictType dictType);

    SysDictVO selectDictInfoByType(String dictType);

}




