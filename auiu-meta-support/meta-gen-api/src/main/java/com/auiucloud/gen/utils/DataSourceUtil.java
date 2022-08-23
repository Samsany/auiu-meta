package com.auiucloud.gen.utils;

import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.utils.StringUtils;
import com.baomidou.mybatisplus.annotation.DbType;

import static com.auiucloud.core.common.utils.StringPool.COLON;
import static com.auiucloud.core.common.utils.StringPool.SLASH;

/**
 * @author dries
 * @createDate 2022-08-23 11-59
 */
public class DataSourceUtil {

    /**
     * TODO 兼容不同数据库连接配置, 暂时只支持mysql
     *
     * @param dbType       数据库类型
     * @param url          ip
     * @param port         端口号
     * @param databaseName 数据库名称
     * @param params       JDBC参数
     * @return url
     */
    public static String DATA_SOURCE_URL(String dbType, String url, String port, String databaseName, String params) {
        String dataSourceUrl = "";
        if (dbType.equals(DbType.MYSQL.getDb())) {
            dataSourceUrl = CommonConstant.JDBC + COLON +
                    dbType + COLON + SLASH + SLASH + url +
                    COLON + port + SLASH +
                    databaseName +
                    StringUtils.parseJsonToUrlParams(params);
        }
        return dataSourceUrl;
    }

}
