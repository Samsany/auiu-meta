package com.auiucloud.gen.service;

import com.auiucloud.gen.domain.GenTable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.util.List;

/**
 * @author dries
 * @description 针对表【gen_table(代码生成业务表)】的数据库操作Service
 * @createDate 2022-08-15 23:44:26
 */
public interface IGenTableService extends IService<GenTable> {

    List<TableInfo> selectDbTableListById(Long dataSourceId);

}
