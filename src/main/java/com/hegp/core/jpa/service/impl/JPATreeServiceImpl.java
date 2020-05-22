package com.hegp.core.jpa.service.impl;

import cn.hutool.core.codec.Base62;
import cn.hutool.core.convert.Convert;
import com.github.wenhao.jpa.Specifications;
import com.hegp.core.exception.ResourcesNotFoundException;
import com.hegp.core.id.generator.SnowflakeId;
import com.hegp.core.jpa.SQLRepository;
import com.hegp.core.jpa.entity.TreeEntity;
import com.hegp.core.jpa.service.JPATreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author hgp
 * @date 20-5-22
 */
@Transactional(readOnly = true)
public class JPATreeServiceImpl<T extends TreeEntity, ID> extends JPAServiceImpl<T, ID> implements JPATreeService<T, ID> {
    @Autowired
    private SQLRepository sqlRepository;

    @Override
    public T save(T entity) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (ObjectUtils.isEmpty(entity.getId())) {
            entity.setCreateAt(now);
        }
        entity.setUpdateAt(now);
        String parentId = entity.getParentId();
        Long snowflakeId = SnowflakeId.id();
        String uniqueCode = Base62.encode(Convert.longToBytes(snowflakeId));
        entity.setUniqueCode(uniqueCode);
        if (StringUtils.hasText(parentId)) {
            T parent = Optional.ofNullable(find((ID)parentId)).orElseThrow(new ResourcesNotFoundException("父级不存在或者已删除"));
            String fullPath = parent.getFullPath();
            entity.setFullPath(StringUtils.hasText(fullPath)? fullPath+uniqueCode+",":uniqueCode+",");
            Integer level = parent.getLevel();
            entity.setLevel(level!=null? level+1:1);
            entity.setParentId(parentId);
        } else {
            entity.setFullPath(uniqueCode+",");
            entity.setParentId(null);
            entity.setLevel(1);
        }
        entity.setOrderNo(queryNextOrderNo());
        entity.setDel(false);
        super.save(entity);
        return entity;
    }

    @Override
    public Integer queryMaxOrderNo() {
        String sql = " SELECT max(order_no) FROM "+entityClassTableNameMap.get(currentEntityClass);
        Long orderIndex = sqlRepository.queryResultCount(sql);
        return orderIndex==null? 1:orderIndex.intValue();
    }

    @Override
    public Integer queryNextOrderNo() {
        return queryMaxOrderNo()+1;
    }

    @Override
    public void exchangeOrder(ID id1, ID id2) {
        T o1 = Optional.ofNullable(find(id1)).orElseThrow(new ResourcesNotFoundException("id为 "+id1+" 的记录不存在或者已删除"));
        T o2 = Optional.ofNullable(find(id2)).orElseThrow(new ResourcesNotFoundException("id为 "+id2+" 的记录不存在或者已删除"));
        Assert.isTrue(o1.getParentId()==o2.getParentId(), "不同级不能移动顺序");
        exchangeOrder(o1, o2);
    }

    public void exchangeOrder(T o1, T o2) {
        Integer orderNo = o1.getOrderNo();
        o1.setOrderNo(o2.getOrderNo());
        o2.setOrderNo(orderNo);
        save(o1);
        save(o2);
    }

    @Override
    public T queryLessOne(T current) {
        return queryLessOrGreaterOne(current, true);
    }

    public T queryLessOrGreaterOne(T current, boolean lessSortOrder) {
        Specification specification = Specifications.and()
                .ne("orderNo", null)
                .eq(StringUtils.isEmpty(current.getParentId()), "parentId", null)
                .eq(StringUtils.hasText(current.getParentId()), "parentId", current.getParentId())
                .lt(lessSortOrder, "orderNo", current.getOrderNo())
                .gt(lessSortOrder==false, "orderNo", current.getOrderNo())
                .build();
        Sort.Direction sort = lessSortOrder? Sort.Direction.DESC: Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(0,1, sort,"orderNo");
        Page<T> pageData = simpleJpaRepository.findAll(specification, pageable);
        return ObjectUtils.isEmpty(pageData.getContent())? null:pageData.getContent().get(0);
    }

    @Override
    public T queryGreaterOne(T current) {
        return queryLessOrGreaterOne(current, false);
    }

    @Override
    @Transactional
    public void upById(ID id) {
        T self = Optional.ofNullable(find(id)).orElseThrow(new ResourcesNotFoundException());
        T lessOne = queryLessOne(self);
        if (lessOne!=null) {
            exchangeOrder(self, lessOne);
        }
    }

    @Override
    @Transactional
    public void downById(ID id) {
        T self = Optional.ofNullable(find(id)).orElseThrow(new ResourcesNotFoundException());
        T greaterOne = queryGreaterOne(self);
        if (greaterOne!=null) {
            exchangeOrder(self, greaterOne);
        }
    }
}
