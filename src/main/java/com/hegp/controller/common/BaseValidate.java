package com.hegp.controller.common;

import com.hegp.core.exception.BusinessException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

public class BaseValidate {

    /**
     * 参数的非空校验
     * @param object
     * @param requiredParams
     */
    public void checkParams(Object object, String... requiredParams) {
        if (requiredParams == null || requiredParams.length < 1) {
            throw new BusinessException("required fields not allow null");
        }

        if (object instanceof Map) { // 校验map的非空参数
            Map paramsMap = (Map) object;
            Arrays.stream(requiredParams).forEach(arg -> {
                if (!paramsMap.containsKey(arg)) {
                    throw new BusinessException("缺少参数" + arg);
                } else if (ObjectUtils.isEmpty(paramsMap.get(arg))) {
                    throw new BusinessException(arg + "不允许为空");
                }
            });
        } else { // 校验DTO的非空参数
            Class clazz = object.getClass();
            for (String fieldName : requiredParams) {
                Field field = ReflectionUtils.findField(clazz, fieldName);
                field.setAccessible(true);
                Object value = ReflectionUtils.getField(field, object);
                if (value == null || value.toString().length()<1) {
                    throw new BusinessException(fieldName + "不允许为空");
                } else if (value instanceof Object[]) {
                    Object[] arr = (Object[]) value;
                    if (arr.length == 0) {
                        throw new BusinessException(fieldName + "不允许为空");
                    }
                } else if (value instanceof List) {
                    List list = (List)value;
                    if (list.size()<1) {
                        throw new BusinessException(fieldName + "不允许为空");
                    }
                } else if (value instanceof Set) {
                    Set set = (Set)value;
                    if (set.size()<1) {
                        throw new BusinessException(fieldName + "不允许为空");
                    }
                }
            }
        }
    }

    public String[] listToStringArr(List<String> list) {
        if (list!=null && list.size()>0) {
            Set<String> set = new HashSet<>(list);
            set.remove(null);
            return set.toArray(new String[]{});
        }
        return new String[0];
    }

}