package com.auiucloud.gen.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.common.utils.StringUtils;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.gen.constant.GenConstants;
import com.auiucloud.gen.domain.GenTable;
import com.auiucloud.gen.domain.GenTableColumn;
import com.auiucloud.gen.domain.SysDataSource;
import com.auiucloud.gen.dto.GenTableDTO;
import com.auiucloud.gen.mapper.GenTableMapper;
import com.auiucloud.gen.service.IGenTableColumnService;
import com.auiucloud.gen.service.IGenTableService;
import com.auiucloud.gen.service.ISysDataSourceService;
import com.auiucloud.gen.utils.DataSourceUtil;
import com.auiucloud.gen.utils.GenUtils;
import com.auiucloud.gen.utils.VmUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author dries
 * @description 针对表【gen_table(代码生成业务表)】的数据库操作Service实现
 * @createDate 2022-08-15 23:44:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements IGenTableService {

    private final ISysDataSourceService dataSourceService;
    private final IGenTableColumnService genTableColumnService;

    @Override
    public PageUtils listPage(Search search, GenTable genTable) {
        LambdaQueryWrapper<GenTable> queryWrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(genTable.getTableName())) {
            queryWrapper.eq(GenTable::getTableName, genTable.getTableName());
        }
        if (StrUtil.isNotBlank(genTable.getTableComment())) {
            queryWrapper.like(GenTable::getTableComment, genTable.getTableComment());
        }
        if (StrUtil.isNotBlank(search.getStartDate())) {
            queryWrapper.ge(GenTable::getCreateTime, search.getStartDate());
        }
        if (StrUtil.isNotBlank(search.getEndDate())) {
            queryWrapper.le(GenTable::getCreateTime, search.getEndDate());
        }

        queryWrapper.orderByDesc(GenTable::getUpdateTime)
                .orderByDesc(GenTable::getCreateTime);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public List<TableInfo> selectDbTableListByDsName(String dsName, GenTable genTable) {

        SysDataSource dataSource = dataSourceService.getDataSourceByDsName(dsName);
        DataSourceConfig dsc = new DataSourceConfig.Builder(
                DataSourceUtil.DATA_SOURCE_URL(
                        dataSource.getDbType(),
                        dataSource.getUrl(),
                        dataSource.getPort(),
                        dataSource.getDatabaseName(),
                        dataSource.getJdbcParams()
                ),
                dataSource.getUsername(),
                dataSource.getPassword()
        ).build();

        ConfigBuilder configBuilder = new ConfigBuilder(null, dsc, null, null, null, null);
        List<TableInfo> tableInfoList = configBuilder.getTableInfoList();
        String tableName = genTable.getTableName();
        String tableComment = genTable.getTableComment();

        if (StrUtil.isNotEmpty(tableName) && StrUtil.isNotEmpty(tableComment)) {
            return tableInfoList
                    .stream().filter(tableInfo ->
                            tableInfo.getName().contains(tableName)
                                    && tableInfo.getComment().contains(tableComment)
                    )
                    .collect(Collectors.toList());
        }

        if (StrUtil.isNotEmpty(tableName)) {
            return tableInfoList
                    .stream().filter(tableInfo ->
                            tableInfo.getName().contains(tableName)
                    )
                    .collect(Collectors.toList());
        }

        if (StrUtil.isNotEmpty(tableComment)) {
            return tableInfoList
                    .stream().filter(tableInfo ->
                            tableInfo.getComment().contains(tableComment)
                    )
                    .collect(Collectors.toList());
        }

        return tableInfoList;
    }

    @DS("#dsName")
    @Override
    public List<GenTable> selectDbTableListByNames(String dsName, String[] tableNames) {
        return baseMapper.selectDbTableListByNames(tableNames);
    }

    @Override
    public void removeByTableNames(String[] tables) {
        LambdaQueryWrapper<GenTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GenTable::getTableName, Arrays.asList(tables));
        List<GenTable> genTableList = this.list(queryWrapper);

        // 批量删除
        List<Long> tableIds = genTableList.stream()
                .map(GenTable::getId)
                .collect(Collectors.toList());
        this.removeBatchByIds(tableIds);
        genTableColumnService.removeBatchByTableIds(tableIds);
    }

    @Override
    public GenTableDTO getGenTableDTOById(Long tableId) {
        GenTable genTable = getById(tableId);
        GenTableDTO genTableDTO = new GenTableDTO();
        BeanUtils.copyProperties(genTable, genTableDTO);

        // 查询字段
        List<GenTableColumn> genTableColumnList = genTableColumnService.selectTableColumnsByTableId(genTable.getId());
        genTableDTO.setColumns(genTableColumnList);

        // 扩展字段填充
        String options = genTable.getOptions();
        if (StrUtil.isNotBlank(options)) {
            JSONObject obj = JSONUtil.parseObj(options);
            String parentMenuId = obj.getStr(GenConstants.PARENT_MENU_ID);
            genTableDTO.setParentMenuId(parentMenuId);
            Integer superEntityClass = obj.getInt(GenConstants.SUPER_ENTITY_CLASS, CommonConstant.STATUS_NORMAL_VALUE);
            genTableDTO.setSuperEntityClass(superEntityClass);
            Integer enableSwagger = obj.getInt(GenConstants.ENABLE_SWAGGER, CommonConstant.STATUS_NORMAL_VALUE);
            genTableDTO.setEnableSwagger(enableSwagger);
        }

        return genTableDTO;
    }

    @Override
    public void importGenTable(String dsName, List<GenTable> tableList) {
        for (GenTable genTable : tableList) {
            GenUtils.initTable(genTable);
            boolean save = this.save(genTable);
            if (save) {
                // 保存列信息
                List<GenTableColumn> tableColumnList = genTableColumnService.selectDbTableColumnsByTableName(dsName, genTable.getTableName());
                for (GenTableColumn column : tableColumnList) {
                    GenUtils.initColumnField(column, genTable);
                    genTableColumnService.save(column);
                }
            }
        }
    }

    @Override
    @Transactional
    public boolean editGenTableById(GenTableDTO genTableDTO) {
        GenTable genTable = new GenTable();
        BeanUtils.copyProperties(genTableDTO, genTable);
        boolean result = this.updateById(genTable);
        if (result) {
            genTableColumnService.updateBatchById(genTableDTO.getColumns());
        }
        return result;
    }

    @Override
    public Map<String, Object> previewCode(Long tableId) {
        Map<String, Object> dataMap = new LinkedHashMap<>();
        GenTableDTO genTable = this.getGenTableDTOById(tableId);
        // 设置主子表信息
        // this.setSubTable(genTableDTO);
        // 设置主键列信息
        this.setPkColumn(genTable);
        VmUtils.initVelocity();

        VelocityContext context = VmUtils.prepareContext(genTable);

        // 获取模板列表
        List<String> templates = VmUtils.getTemplateList(genTable.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, StringPool.UTF_8);
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    /**
     * 设置主键列信息
     *
     * @param table 业务表信息
     */
    private void setPkColumn(GenTableDTO table) {
        for (GenTableColumn column : table.getColumns()) {
            if (GenUtils.isChecked(column.getIsPk())) {
                table.setPkColumn(column);
                break;
            }
        }
        if (StringUtils.isNull(table.getPkColumn())) {
            table.setPkColumn(table.getColumns().get(0));
        }
        if (GenConstants.TPL_SUB.equals(table.getTplCategory())) {
            for (GenTableColumn column : table.getSubTable().getColumns()) {
                if (GenUtils.isChecked(column.getIsPk())) {
                    table.getSubTable().setPkColumn(column);
                    break;
                }
            }
            if (StringUtils.isNull(table.getSubTable().getPkColumn())) {
                table.getSubTable().setPkColumn(table.getSubTable().getColumns().get(0));
            }
        }
    }

    @Override
    public void generatorCode(Long tableId) {
        // 查询表信息
        GenTableDTO genTable = this.getGenTableDTOById(tableId);
        // 设置主子表信息
        // setSubTable(table);
        // 设置主键列信息
        setPkColumn(genTable);

        VmUtils.initVelocity();
        VelocityContext context = VmUtils.prepareContext(genTable);

        // 获取模板列表
        List<String> templates = VmUtils.getTemplateList(genTable.getTplCategory());
        for (String template : templates) {
            if (!StringUtils.containsAny(template, "sql.vm", "api.js.vm", "index.vue.vm", "index-tree.vue.vm")) {
                // 渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, StringPool.UTF_8);
                tpl.merge(context, sw);
                try {
                    String path = getGenPath(genTable, template);
                    FileUtils.writeStringToFile(new File(path), sw.toString(), StringPool.UTF_8);
                } catch (IOException e) {
                    throw new ApiException("渲染模板失败，表名：" + genTable.getTableName());
                }
            }
        }
    }

    /**
     * 获取代码生成地址
     *
     * @param genTable 业务表信息
     * @param template 模板文件路径
     * @return 生成地址
     */
    private String getGenPath(GenTableDTO genTable, String template) {
        String genPath = genTable.getGenPath();
        if (StringUtils.equals(genPath, "/")) {
            return System.getProperty("user.dir") + File.separator + "src" + File.separator + VmUtils.getFileName(template, genTable);
        }
        return genPath + File.separator + VmUtils.getFileName(template, genTable);
    }

    @Override
    public byte[] downloadGenCode(Long tableId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(tableId, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    @Override
    public byte[] downloadGenCode(Long[] tableIds) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (Long tableId : tableIds) {
            generatorCode(tableId, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询表信息并生成代码
     */
    private void generatorCode(Long tableId, ZipOutputStream zip) {
        // 查询表信息
        GenTableDTO genTable = this.getGenTableDTOById(tableId);
        // 设置主子表信息
        // setSubTable(table);
        // 设置主键列信息
        setPkColumn(genTable);

        VmUtils.initVelocity();

        VelocityContext context = VmUtils.prepareContext(genTable);

        // 获取模板列表
        List<String> templates = VmUtils.getTemplateList(genTable.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, StringPool.UTF_8);
            tpl.merge(context, sw);
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VmUtils.getFileName(template, genTable)));
                IOUtils.write(sw.toString(), zip, StringPool.UTF_8);
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + genTable.getTableName(), e);
            }
        }
    }

    // private void setSubTable(GenTableDTO table) {
    //     String subTableName = table.getSubTableName();
    //     if (StrUtil.isNotEmpty(subTableName)) {
    //         // genTableDTO.setSubTable(baseMapper.selectGenTableByName(subTableName));
    //     }
    // }
}




