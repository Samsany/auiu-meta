package com.auiucloud.gen.service;

import com.auiucloud.gen.domain.GenTableColumn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author dries
 * @description 针对表【gen_table_column(代码生成业务表字段)】的数据库操作Service
 * @createDate 2022-08-15 23:44:26
 */
public interface IGenTableColumnService extends IService<GenTableColumn> {

    /**
     * 根据数据库表名查询数据库表字段
     *
     * @param dsName    数据源名称
     * @param tableName 表名称
     * @return List<GenTableColumn>
     */
    List<GenTableColumn> selectDbTableColumnsByTableName(String dsName, String tableName);

    /**
     * 根据数据库表ID批量删除表字段
     *
     * @param tableIds 数据库表ID
     */
    void removeBatchByTableIds(List<Long> tableIds);

    List<GenTableColumn> selectTableColumnsByTableId(Long tableId);

}
