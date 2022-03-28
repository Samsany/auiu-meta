package com.auiucloud.core.database.utils;

import com.auiucloud.core.database.model.Search;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页工具类
 *
 * @author dries
 * @date 2021/12/21
 */
public class PageUtil {

    /**
     * @param search 分页参数
     * @return T
     */
    public static <T> IPage<T> getPage(Search search) {
        return new Page<T>(search.getPageNum(), search.getPageSize());
    }

}
