package com.hegp.core.jpa;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SQLRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Map> assemblyPageData(String dataSQL, String countSQL, int page, int pagesize, Object... params) {
        if (pagesize<1) {
            throw new RuntimeException("pagesize必须大于0");
        }
        Long totalCount = queryResultCount(countSQL, params);
        List<Map> data = new ArrayList<>();
        if (totalCount!=0) {
            data = queryPageResultList(dataSQL, page, pagesize, params);
        }
        return new PageImpl(data, PageRequest.of(page, pagesize), totalCount);
    }

    /** 前端传过来的页码是从1开始的，entityManager.createNativeQuery的查询的*/
    public List<Map> queryPageResultList(String sql, int page, int pagesize, Object... params) {
        Query dataQuery = entityManager.createNativeQuery(sql);
        assemblyParam(dataQuery, params).unwrap(NativeQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setFirstResult((page-1)*pagesize)
                .setMaxResults(pagesize);
        return (List<Map>) dataQuery.getResultList()
                .stream().map(item -> convertKeyToCamel((Map<String, Object>) item))
                .collect(Collectors.toList());
    }

    /** 装配Sql,返回全部结果 */
    public List<Map> queryResultList(String sql, Object... params) {
        Query dataQuery = entityManager.createNativeQuery(sql);
        assemblyParam(dataQuery, params).unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (List<Map>)dataQuery.getResultList()
                .stream().map(item -> convertKeyToCamel((Map<String, Object>) item))
                .collect(Collectors.toList());
    }

    public Map queryResultMap(String sql, Object... params) {
        List<Map> data = queryResultList(sql, params);
        if(data.size() != 0) {
            return data.get(0);
        }
        return null;
    }

    /** 返回SQL查询的数据条数 */
    public Long queryResultCount(String sql, Object... params) {
        Query countQuery = entityManager.createNativeQuery(sql);
        assemblyParam(countQuery, params);
        return Long.parseLong(countQuery.getSingleResult().toString());
    }

    /** 执行修改语句，返回受影响行数 */
    public Integer queryModifyCount(String sql, Object... params){
        EntityTransaction transaction = entityManager.getTransaction();
        Query dataQuery = entityManager.createNativeQuery(sql);
        int count = assemblyParam(dataQuery, params).executeUpdate();
        transaction.commit();
        return count;
    }

    /** 为Sql填充参数 */
    private static Query assemblyParam(Query query, Object... params) {
        int index = 1;
        for (Object param : params) {
            if (param!=null && param instanceof Collection) {
                if (((Collection) param).size()>0) {
                    query.setParameter(index, param);
                }
            } else if (!StringUtils.isEmpty(param)) {
                query.setParameter(index, param);
            }
            index++;
        }
        return query;
    }

    /**
     * 把map的key转换为驼峰命名
     * @param map map对象
     * @return 返回转换后的值
     */
    private static Map convertKeyToCamel(Map map){
        if(map==null) {
            return null;
        }
        Map linkedHashMap = new LinkedHashMap();
        map.forEach((key, value) -> linkedHashMap.put(convert(key.toString()), value));
        return linkedHashMap;
    }

    /**
     * 数据库查出来的字段，下划线转驼峰
     * @param defaultName
     * @return
     */
    private static String convert(String defaultName) {
        char[] arr = defaultName.toCharArray();
        StringBuilder nameToReturn = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '_') {
                nameToReturn.append(Character.toUpperCase(arr[++i]));
            } else {
                nameToReturn.append(arr[i]);
            }
        }
        return nameToReturn.toString();
    }
}
