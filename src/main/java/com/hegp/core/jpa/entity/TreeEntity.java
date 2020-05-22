package com.hegp.core.jpa.entity;

import javax.persistence.MappedSuperclass;

/**
 * @author hgp
 * @date 20-5-22
 */
@MappedSuperclass
public class TreeEntity extends IdEntity {
    private String parentId;
    private Integer orderNo;
    // fullPath拼接的路径是每个父项的uniqueCode拼接起来
    private String uniqueCode;
    private String fullPath;
    private Integer level;

    public TreeEntity() {
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
