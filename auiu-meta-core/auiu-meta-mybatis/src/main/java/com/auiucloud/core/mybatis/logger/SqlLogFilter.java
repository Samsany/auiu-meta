package com.auiucloud.core.mybatis.logger;

import com.alibaba.druid.DbType;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.StringUtils;
import com.auiucloud.core.mybatis.props.MateMybatisProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 打印可执行的 sql 日志
 *
 * @author dries
 * @date 2021/12/21
 */
@Slf4j
@RequiredArgsConstructor
public class SqlLogFilter extends FilterEventAdapter {
    private static final SQLUtils.FormatOption FORMAT_OPTION = new SQLUtils.FormatOption(false, false);
    private final MateMybatisProps mateMybatisProps;

    @Override
    protected void statementExecuteUpdateBefore(StatementProxy statement, String sql) {
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteQueryBefore(StatementProxy statement, String sql) {
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteBefore(StatementProxy statement, String sql) {
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean result) {
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteBatchBefore(StatementProxy statement) {
        statement.setLastExecuteStartNano();
    }

    @Override
    protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
        statement.setLastExecuteStartNano();
    }

    @Override
    public void statement_close(FilterChain chain, StatementProxy statement) throws SQLException {
        // 支持动态开启
        if (!mateMybatisProps.isSql()) {
            return;
        }
        // 是否开启调试
        if (!log.isInfoEnabled()) {
            return;
        }

        // 打印可执行的 sql
        String sql = statement.getBatchSql();
        // sql 为空直接返回
        if (StringUtils.isEmpty(sql)) {
            return;
        }
        int parametersSize = statement.getParametersSize();
        List<Object> parameters = new ArrayList<>(parametersSize);
        for (int i = 0; i < parametersSize; ++i) {
            JdbcParameter jdbcParam = statement.getParameter(i);
            parameters.add(jdbcParam != null ? jdbcParam.getValue() : null);
        }
        String dbType = statement.getConnectionProxy().getDirectDataSource().getDbType();
        String formattedSql = SQLUtils.format(sql, DbType.of(dbType), parameters, FORMAT_OPTION);
        printSql(formattedSql, statement);
    }

    private void printSql(String sql, StatementProxy statement) {
        // 打印 sql
        String sqlLogger = "\n\n==============  Sql Start  ==============" +
                "\nExecute SQL ：{}" +
                "\nExecute Time：{}" +
                "\n==============  Sql  End   ==============\n";
        log.info(sqlLogger, sql.trim(), statement.getLastExecuteTimeNano());
    }

}
