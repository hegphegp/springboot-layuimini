package com.hegp.core.sql;

import com.github.pagehelper.parser.CountSqlParser;
import com.github.pagehelper.parser.SqlServerParser;

/**
 * @author hgp
 * @date 20-5-18
 */
public class PageSql {

    private static CountSqlParser countSqlParser = new CountSqlParser();
    private static SqlServerParser sqlServerParser = new SqlServerParser();

    public String getCountSql(String sql) {
        return countSqlParser.getSmartCountSql(sql);
    }

    // 原本想直接使用 com.github.pagehelper.dialect.helper 包下的getPageSql()方法,
    // 但是该方法的参数类型是 org.apache.ibatis.cache.CacheKey, 必须引入mybatis的依赖
    // 这段代码都是直接使用 com.github.pagehelper.dialect.helper包下各种语言的分页SQL的逻辑代码
    public String getPageSql(String sql, String driverType, int startRow, int endRow) {
        switch (driverType) {
            case "MYSQL":
                return getMysqlPageSql(sql, startRow, endRow);
            case "POSTGRESQL":
                return getPostgresPageSql(sql, startRow, endRow);
            case "ORACLE":
                return getOraclePageSql(sql, startRow, endRow);
            case "DB2":
                getDB2PageSql(sql, startRow, endRow);
            case "SQLSERVER":
                sqlServerParser.convertToPageSql(sql, startRow, endRow-startRow);
            case "IFX":
                return getIfxDriverPageSql(sql, startRow, endRow);
        }
        return sql;
    }

    public String getIfxDriverPageSql(String sql, int startRow, int endRow) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 40);
        sqlBuilder.append("SELECT ");
        if (startRow > 0) {
            sqlBuilder.append(" SKIP "+startRow);
        }
        if (endRow > 0) {
            sqlBuilder.append(" FIRST "+endRow);
        }
        sqlBuilder.append(" * FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) TEMP_T ");
        return sqlBuilder.toString();
    }

    public String getDB2PageSql(String sql, int startRow, int endRow) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) AS TMP_PAGE) TMP_PAGE WHERE ROW_ID BETWEEN "+startRow+" AND "+endRow);
        return sqlBuilder.toString();
    }

    public String getOraclePageSql(String sql, int startRow, int endRow) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("SELECT * FROM ( ");
        sqlBuilder.append(" SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) TMP_PAGE)");
        sqlBuilder.append(" WHERE ROW_ID <= "+startRow+" AND ROW_ID > "+endRow);
        return sqlBuilder.toString();
    }

    public String getPostgresPageSql(String sql, int startRow, int endRow) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(sql);
        if (endRow > 0) {
            sqlBuilder.append(" LIMIT "+(endRow-startRow));
        }
        if (startRow > 0) {
            sqlBuilder.append(" OFFSET "+startRow);
        }
        return sqlBuilder.toString();
    }

    public String getMysqlPageSql(String sql, int startRow, int endRow) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        if (startRow == 0) {
            sqlBuilder.append(" LIMIT "+endRow);
        } else {
            sqlBuilder.append(" LIMIT "+startRow+", "+endRow);
        }
        return sqlBuilder.toString();
    }

    public static void main(String[] args) {
        String[] driverTypes = new String[] {
            "MYSQL",
            "POSTGRESQL",
            "ORACLE",
            "DB2",
            "SQLSERVER",
            "IDX"
        };

        PageSql pageSql = new PageSql();
        String sql = "SELECT * FROM sys_user ORDER BY username";
        int startRow = 0;
        int endRow = 10;
        for (String driverType:driverTypes) {
            System.out.println(driverType+"  ==>>  " + pageSql.getPageSql(sql, driverType, startRow, endRow));
        }

        System.out.println("\n\n\n");

        startRow = 90;
        endRow = 100;
        for (String driverType:driverTypes) {
            System.out.println(driverType+"  ==>>  " + pageSql.getPageSql(sql, driverType, startRow, endRow));
        }
    }
}
