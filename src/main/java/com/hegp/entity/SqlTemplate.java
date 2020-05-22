package com.hegp.entity;

import com.hegp.core.jpa.entity.TreeEntity;

import javax.persistence.*;

/**
 * 搞成树状结构的存储, 在前端页面展示成树状结构, 父节点按照每个系统或者模块划分
 * @author hgp
 * @date 20-5-20
 */
@Entity
@Table(name = "gs_sql_template",
       indexes={@Index(name="gs_sql_template_order_no_index", columnList="orderNo", unique=false)})
public class SqlTemplate extends TreeEntity {
    private String name;
    private String method; // http的method
    private String description;
    private String url;
    @Column(length = 10240)
    private String sqlTemplate;
    private String pageIndexField;
    private String pageSizeField;
    private String totalPageField;
    private Boolean pagination; // 是否是分页接口
    @Column(length = 10240)
    private String params;
    private Boolean useMock=false;
    // mock数据允许保存50K
    @Column(length = 51200)
    private String mockData;

    public SqlTemplate() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSqlTemplate() {
        return sqlTemplate;
    }

    public void setSqlTemplate(String sqlTemplate) {
        this.sqlTemplate = sqlTemplate;
    }

    public String getPageIndexField() {
        return pageIndexField;
    }

    public void setPageIndexField(String pageIndexField) {
        this.pageIndexField = pageIndexField;
    }

    public String getPageSizeField() {
        return pageSizeField;
    }

    public void setPageSizeField(String pageSizeField) {
        this.pageSizeField = pageSizeField;
    }

    public String getTotalPageField() {
        return totalPageField;
    }

    public void setTotalPageField(String totalPageField) {
        this.totalPageField = totalPageField;
    }

    public Boolean getPagination() {
        return pagination;
    }

    public void setPagination(Boolean pagination) {
        this.pagination = pagination;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Boolean getUseMock() {
        return useMock;
    }

    public void setUseMock(Boolean useMock) {
        this.useMock = useMock;
    }

    public String getMockData() {
        return mockData;
    }

    public void setMockData(String mockData) {
        this.mockData = mockData;
    }

}
