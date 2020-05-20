package com.hegp.core.utils;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hgp
 * @date 20-5-20
 */
public class BeanMapUtils {

    public static <T> T mapToBean(Map map, Class<T> clazz) throws Exception {
        try {
            if (map==null) return null;
            T bean = clazz.newInstance();
            BeanMap beanMap = BeanMap.create(bean);
            beanMap.putAll(map);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> mapListToBeanList(List<Map> list, Class<T> clazz) {
        try {
            List<T> returnList = new ArrayList();
            if (ObjectUtils.isEmpty(list)) return returnList;
            for (Map map : list) {
                T bean = clazz.newInstance();
                BeanMap beanMap = BeanMap.create(bean);
                beanMap.putAll(map);
                returnList.add(bean);
            }
            return returnList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
