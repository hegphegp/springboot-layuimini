package com.hegp.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 搞成树状结构的存储, 在前端页面展示成树状结构, 父节点按照每个系统或者模块划分
 * @author hgp
 * @date 20-5-20
 */
public class SqlTemplateDTO {
    private String id;
    private String name;
    private String method; // http的method
    private String description;
    private String url;
    private String sqlTemplate;
    private String pageIndexField;
    private String pageSizeField;
    private String totalPageField;
    private Boolean pagination;
    private List<Param> params;
    private Boolean useMock;
    private Map<String,Object> mockData;
    private String parentId;
    private Integer orderIndex;
    private Timestamp createAt;
    private Timestamp updateAt;

    public SqlTemplateDTO() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public Boolean getUseMock() {
        return useMock;
    }

    public void setUseMock(Boolean useMock) {
        this.useMock = useMock;
    }

    public Map<String,Object> getMockData() {
        return mockData;
    }

    public void setMockData(Map<String,Object> mockData) {
        this.mockData = mockData;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }
}
