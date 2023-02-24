package com.auiucloud.core.common.utils.sql;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.exception.ApiException;

import java.util.List;

/**
 * Sql过滤
 *
 * @author dries
 * @createDate 2022-06-30 23-56
 */
public class SqlUtil {

    /**
     * 定义常用的 sql关键字
     */
    public static String SQL_REGEX = "select,insert,delete,update,drop,count,exec,chr,mid,master,truncate,char,and,declare";
    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     */
    public static String sqlInject(String str) {

        if (StrUtil.isBlank(str)) {
            return null;
        }

        if (StrUtil.isNotEmpty(str) && !isValidOrderBySql(str)) {
            throw new ApiException("参数不符合规范，不能进行查询");
        }

        // 去掉'|"|;|\字符
        str = StrUtil.replace(str, "'", "");
        str = StrUtil.replace(str, "\"", "");
        str = StrUtil.replace(str, ";", "");
        str = StrUtil.replace(str, "\\", "");

        // 转换成小写
        str = str.toLowerCase();

        // 非法字符
        List<String> keywords = StrUtil.split(SQL_REGEX, ",");

        // 判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.contains(keyword)) {
                throw new ApiException("包含非法字符");
            }
        }

        return str;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

}
