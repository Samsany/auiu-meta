package com.auiucloud.gen.service.impl;

import com.auiucloud.gen.domain.GenTableColumn;
import com.auiucloud.gen.mapper.GenTableColumnMapper;
import com.auiucloud.gen.service.IGenTableColumnService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dries
 * @description 针对表【gen_table_column(代码生成业务表字段)】的数据库操作Service实现
 * @createDate 2022-08-15 23:44:26
 */
@Service
public class GenTableColumnServiceImpl extends ServiceImpl<GenTableColumnMapper, GenTableColumn>
        implements IGenTableColumnService {

    @DS("#dsName")
    @Override
    public List<GenTableColumn> selectDbTableColumnsByTableName(String dsName, String tableName) {
        return baseMapper.selectDbTableColumnsByTableName(tableName);
    }

    @Override
    public void removeBatchByTableIds(List<Long> tableIds) {
        LambdaQueryWrapper<GenTableColumn> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GenTableColumn::getTableId, tableIds);
        this.remove(queryWrapper);
    }

    @Override
    public List<GenTableColumn> selectTableColumnsByTableId(Long tableId) {
        LambdaQueryWrapper<GenTableColumn> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GenTableColumn::getTableId, tableId);
        return this.list(queryWrapper);
    }
}




