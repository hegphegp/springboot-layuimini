package com.hegp.core.jpa.service;

import com.hegp.core.jpa.entity.TreeEntity;

/**
 * @author hgp
 * @date 20-5-22
 */
public interface JPATreeService<T extends TreeEntity, ID> extends JPAService<T, ID> {
    /** 组织机构在同级的排序 */
    Integer queryMaxOrderNo();
    Integer queryNextOrderNo();
    void exchangeOrder(ID id1, ID id2);
    T queryLessOne(T current);
    T queryGreaterOne(T current);
    void upById(ID id);
    void downById(ID id);
}
