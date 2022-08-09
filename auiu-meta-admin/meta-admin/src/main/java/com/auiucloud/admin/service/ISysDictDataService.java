package com.auiucloud.admin.service;

import com.auiucloud.admin.domain.SysDictData;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【sys_dict_data(字典数据表)】的数据库操作Service
 * @createDate 2022-07-03 15:02:06
 */
public interface ISysDictDataService extends IService<SysDictData> {

    /**
     * 查询字典数据列表
     *
     * @param search   查询参数
     * @param dictData 字典项数据
     * @return PageUtils 分页数据
     */
    PageUtils listPage(Search search, SysDictData dictData);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据
     */
    List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 根据字典类型查询统计字典数据条数
     *
     * @param dictType 字典类型
     * @return int
     */
    long countDictDataByType(String dictType);

}
