package com.hegp.service;

import com.hegp.core.jpa.service.JPAService;
import com.hegp.core.jpa.service.JPATreeService;
import com.hegp.entity.SqlTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author hgp
 * @date 20-5-20
 */
public interface SqlTemplateService extends JPATreeService<SqlTemplate, String> {
//
//    /** 组织机构在同级的排序 */
//    Long queryMaxOrderNo();
//    Long queryNextOrderNo();
//    void exchangeOrder(String id1, String id2);
//    SqlTemplate queryLessOne(SqlTemplate current);
//    SqlTemplate queryGreaterOne(SqlTemplate current);
//    void upById(String id);
//    void downById(String id);
//
//    /**
//     * 返回list集合部分字段, 然后ztree自动封装list成树状结构
//     * @return
//     */
    List<Map> queryTreeListData();
}
