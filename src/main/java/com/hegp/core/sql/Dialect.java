package com.hegp.core.sql;

public enum Dialect {
    MySql("MySql"),
    Oracle("Oracle"),
    SqlServer("SqlServer"),
    PostgreSQL("PostgreSQL"),
    Db2("Db2");

    private String value;

    Dialect(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
