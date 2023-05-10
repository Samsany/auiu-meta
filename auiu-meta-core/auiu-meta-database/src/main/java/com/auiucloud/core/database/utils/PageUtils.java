package com.auiucloud.core.database.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.enums.OrderTypeEnum;
import com.auiucloud.core.common.utils.sql.SqlUtil;
import com.auiucloud.core.database.model.Search;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;

/**
 * 分页工具类
 *
 * @author dries
 * @date 2021/12/21
 */
@Data
public class PageUtils {

    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页数
     */
    private int pageNum;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 列表数据
     */
    private List<?> list;
    /**
     * 是否有下一页
     */
    private boolean hasNextPage;


    /**
     * Mybatis-Plus 自带分页
     */
    public PageUtils(IPage<?> page) {
        this.list = page.getRecords();
        this.totalCount = Math.toIntExact(page.getTotal());
        this.pageSize = Math.toIntExact(page.getSize());
        this.pageNum = Math.toIntExact(page.getCurrent());
        this.totalPage = Math.toIntExact(page.getPages());
        this.hasNextPage = page.getCurrent() < page.getPages();
    }


    /**
     * pageHelper分页
     */
     public PageUtils(List<?> list) {
         PageInfo<?> pageInfo = new PageInfo<>(list);
         this.list = pageInfo.getList();
         this.totalCount = Math.toIntExact(pageInfo.getTotal());
         this.pageSize = pageInfo.getPageSize();
         this.pageNum = pageInfo.getPageNum();
         this.totalPage = pageInfo.getPages();
         this.hasNextPage = pageInfo.isHasNextPage();
     }

    /**
     * 手工分页
     *
     * @param list   列表数据
     * @param search 查询参数
     */
    public PageUtils(Search search, List<?> list) {
        int pageNum = 1;
        int pageSize = 10;
        if (search.getPageNum() != null) {
            pageNum = search.getPageNum();
        }
        if (search.getPageSize() != null) {
            pageSize = search.getPageSize();
        }

        if (CollUtil.isNotEmpty(list)) {
            this.totalCount = list.size();
            this.pageSize = pageSize;
            this.pageNum = Math.max(pageNum, 1);
            this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
            this.hasNextPage = pageNum < this.totalPage;

            int size = list.size();
            int pageCount = size / pageSize;
            int fromIndex = (this.pageNum - 1) * pageSize;
            int toIndex = fromIndex + pageSize;
            if (toIndex >= size) {
                toIndex = size;
            }
            if (this.pageNum > pageCount + 1) {
                fromIndex = 0;
                toIndex = 0;
            }
            this.list = list.subList(fromIndex, toIndex);
        } else {
            this.totalCount = 0;
            this.pageSize = pageSize;
            this.pageNum = pageNum;
            this.totalPage = 0;
            this.list = Collections.emptyList();
            this.hasNextPage = false;
        }
    }

    /**
     * 手工分页
     *
     * @param list   列表数据
     * @param search 查询参数
     */
    public PageUtils(Long total, Search search, List<?> list) {
        int pageNum = 1;
        int pageSize = 10;
        if (search.getPageNum() != null) {
            pageNum = search.getPageNum();
        }
        if (search.getPageSize() != null) {
            pageSize = search.getPageSize();
        }

        if (CollUtil.isNotEmpty(list)) {
            this.totalCount = Math.toIntExact(total);
            this.pageSize = pageSize;
            this.pageNum = Math.max(pageNum, 1);
            this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
            this.hasNextPage = pageNum < this.totalPage;

            this.list = list;
        } else {
            this.totalCount = 0;
            this.pageSize = pageSize;
            this.pageNum = pageNum;
            this.totalPage = 0;
            this.list = Collections.emptyList();
            this.hasNextPage = false;
        }
    }

    /**
     * 手工分页
     *
     * @param list     列表数据
     * @param pageSize 每页记录数
     * @param pageNum  当前页数
     */
    public PageUtils(int pageNum, int pageSize, List<?> list) {
        if (CollUtil.isNotEmpty(list)) {
            this.totalCount = list.size();
            this.pageSize = pageSize;
            this.pageNum = Math.max(pageNum, 1);
            this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
            this.hasNextPage = pageNum < this.totalPage;

            int size = list.size();
            int pageCount = size / pageSize;
            int fromIndex = (this.pageNum - 1) * pageSize;
            int toIndex = fromIndex + pageSize;
            if (toIndex >= size) {
                toIndex = size;
            }
            if (this.pageNum > pageCount + 1) {
                fromIndex = 0;
                toIndex = 0;
            }
            this.list = list.subList(fromIndex, toIndex);
        } else {
            this.totalCount = 0;
            this.pageSize = pageSize;
            this.pageNum = pageNum;
            this.totalPage = 0;
            this.list = Collections.emptyList();
            this.hasNextPage = false;
        }
    }

    /**
     * @param search 分页参数
     * @return T
     */
    public static <T> IPage<T> getPage(Search search) {
        int pageNum = 1;
        int pageSize = 10;
        if (search.getPageNum() != null) {
            pageNum = search.getPageNum();
        }
        if (search.getPageSize() != null) {
            pageSize = search.getPageSize();
        }
        return new Page<T>(pageNum, pageSize);
    }

    public static void startPage(Search search) {
        String order = search.getOrder();
        String orderByColumn = search.getProp();
        if (StrUtil.isNotBlank(orderByColumn)) {
            String orderBy = SqlUtil.sqlInject(orderByColumn);
            if (StrUtil.isNotBlank(order)) {
                if (OrderTypeEnum.ASC.getValue().equalsIgnoreCase(order) || OrderTypeEnum.DESC.getValue().equalsIgnoreCase(order)) {
                    orderBy = orderBy + " " + order;
                }
            }
            // 开启分页
            PageHelper.startPage(search.getPageNum(), search.getPageSize(), orderBy);
        } else {
            // 开启分页
            PageHelper.startPage(search.getPageNum(), search.getPageSize());
        }
    }

    public static Search buildPage(Search search, int total) {
        Search build = new Search();
        BeanUtils.copyProperties(search, build);
        int pageNum = 1;
        int pageSize = 10;
        if (search.getPageNum() != null) {
            pageNum = search.getPageNum();
        }
        if (search.getPageSize() != null) {
            pageSize = search.getPageSize();
        }

        int pageCount = total / pageSize;
        int fromIndex = (pageNum - 1) * pageSize;
        //        int toIndex = fromIndex + pageSize;
        //        if (toIndex >= total) {
        //            toIndex = total;
        //        }
        if (pageNum > pageCount + 1) {
            fromIndex = 0;
            //            toIndex = 0;
        }

        build.setPageNum(fromIndex);
        build.setPageSize(pageSize);
        return build;
    }
}
