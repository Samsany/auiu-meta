package com.auiucloud.gen.service;

import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.gen.domain.GenTable;
import com.auiucloud.gen.dto.GenTableDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.util.List;
import java.util.Map;

/**
 * @author dries
 * @description 针对表【gen_table(代码生成业务表)】的数据库操作Service
 * @createDate 2022-08-15 23:44:26
 */
public interface IGenTableService extends IService<GenTable> {

    /**
     * 代码生成列表
     *
     * @param search   查询条件
     * @param genTable 查询条件
     * @return PageUtils 分页对象
     */
    PageUtils listPage(Search search, GenTable genTable);

    /**
     * 根据数据源名称查询数据源表列表
     *
     * @param dsName   数据源名称
     * @param genTable 查询参数
     * @return List<TableInfo>
     */
    List<TableInfo> selectDbTableListByDsName(String dsName, GenTable genTable);

    /**
     * 根据表名称查询数剧表信息列表
     *
     * @param dsName     数据源名称
     * @param tableNames 表名称
     * @return List<GenTable>
     */
    List<GenTable> selectDbTableListByNames(String dsName, String[] tableNames);

    /**
     * @param tableId 根据表ID查询表信息
     * @return GenTableDTO
     */
    GenTableDTO getGenTableDTOById(Long tableId);

    /**
     * 导入表数据
     *
     * @param dsName    数据源名称
     * @param tableList 表列表
     */
    void importGenTable(String dsName, List<GenTable> tableList);

    void removeByTableNames(String[] tables);

    /**
     * 代码预览
     *
     * @param tableId 表ID
     * @return Map<String, Object>
     */
    Map<String, Object> previewCode(String tableId);

    boolean editGenTableById(GenTableDTO genTableDTO);
}
