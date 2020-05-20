package com.hegp.service.impl;

import com.hegp.core.jpa.SQLRepository;
import com.hegp.core.jpa.service.impl.JPAServiceImpl;
import com.hegp.core.utils.TreeListUtil;
import com.hegp.entity.SqlTemplate;
import com.hegp.service.SqlTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hgp
 * @date 20-5-20
 */
@Service
public class SqlTemplateServiceImpl extends JPAServiceImpl<SqlTemplate, String> implements SqlTemplateService {

    @Autowired
    private SQLRepository sqlRepository;

    @Override
    public Long queryMaxOrderNo() {
        String sql = " SELECT max(order_no) FROM gs_sql_template ";
        Long orderIndex = sqlRepository.queryResultCount(sql);
        return orderIndex==null? 1L:orderIndex;
    }

    @Override
    public Long queryNextOrderNo() {
        return queryMaxOrderNo()+1;
    }

    @Override
    public void exchangeOrder(String id1, String id2) {

    }

    @Override
    public SqlTemplate queryLessSortOrder(SqlTemplate current) {
        return null;
    }

    @Override
    public SqlTemplate queryGreaterSortOrder(SqlTemplate current) {
        return null;
    }

    @Override
    public void upById(String id) {

    }

    @Override
    public void downById(String id) {

    }

    @Override
    public SqlTemplate find(String id) {
        SqlTemplate sqlTemplate = super.find(id);
        return (sqlTemplate==null || true == sqlTemplate.getDel())? null:sqlTemplate;
    }

    // zTree 不提供排序功能，仅仅按照用户提供的数据顺序进行排列的。
    @Override
    public List<Map> queryTreeListData() {
        String sql = " SELECT id, name, parent_id, order_no, use_mock FROM gs_sql_template WHERE del=false";
        List<Map> list = sqlRepository.queryResultList(sql);
        list = TreeListUtil.buildAscTreeList(list, "id", "parentId", "children", "orderNo");
        return list;
    }
}
