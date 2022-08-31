package com.auiucloud.gen.mapper;

import com.auiucloud.gen.domain.GenTable;
import com.auiucloud.gen.dto.GenTableDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dries
 * @description 针对表【gen_table(代码生成业务表)】的数据库操作Mapper
 * @createDate 2022-08-15 23:44:26
 * @Entity com.auiucloud.gen.domain.GenTable
 */
public interface GenTableMapper extends BaseMapper<GenTable> {

    List<GenTable> selectDbTableListByNames(String[] tableNames);

    GenTableDTO selectGenTableByName(@Param("subTableName") String subTableName);
}




