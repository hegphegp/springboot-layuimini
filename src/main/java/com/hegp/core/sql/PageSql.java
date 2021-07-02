package com.hegp.core.sql;

/**
 * 整个SQL分页解析的代码都是参考 implementation 'com.github.pagehelper:pagehelper:5.1.11' 的
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
    public String getPageSql(String sql, Dialect dialect, int startRow, int endRow) {
        switch (dialect) {
            case H2:
                return getMysqlPageSql(sql, startRow, endRow-startRow);
            case MySql:
                return getMysqlPageSql(sql, startRow, endRow);
            case PostgreSQL:
                return getPostgresPageSql(sql, startRow, endRow);
            case Oracle:
                return getOraclePageSql(sql, startRow, endRow);
            case Db2:
                return getDB2PageSql(sql, startRow, endRow);
            case SqlServer:
                return sqlServerParser.convertToPageSql(sql, startRow, endRow-startRow);
        }
        return sql;
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
        String sql = "SELECT * FROM sys_user ORDER BY username";
        printSQL(sql, 0, 10);
        printSQL(sql, 90, 100);

        sql = "SELECT * FROM sys_user GROUP BY username ORDER BY username";
        printSQL(sql, 0, 10);
        printSQL(sql, 90, 100);

        sql = "SELECT * FROM sys_user WHERE nickname LIKE :nickname GROUP BY username ORDER BY username";
        printSQL(sql, 0, 10);
        printSQL(sql, 90, 100);
    }

    private static void printSQL(String sql, int startRow, int endRow) {
        PageSql pageSql = new PageSql();
        Dialect[] dialects = new Dialect[] {
                Dialect.MySql,
                Dialect.PostgreSQL,
                Dialect.Oracle,
                Dialect.SqlServer,
                Dialect.Db2,
                Dialect.H2
        };

        for (Dialect dialect:dialects) {
            System.out.println(dialect.getValue()+"  ==>>  " + pageSql.getPageSql(sql, dialect, startRow, endRow));
        }
        System.out.println(pageSql.getCountSql(sql));
        System.out.println("\n\n\n");
    }
}
