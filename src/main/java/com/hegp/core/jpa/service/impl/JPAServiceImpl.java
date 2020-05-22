package com.hegp.core.jpa.service.impl;

import com.github.wenhao.jpa.Specifications;
import com.hegp.core.exception.ResourcesNotFoundException;
import com.hegp.core.jpa.entity.IdEntity;
import com.hegp.core.jpa.service.JPAService;
import org.hibernate.metamodel.internal.MetamodelImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.*;

@Transactional(readOnly = true)
public class JPAServiceImpl<T extends IdEntity, ID> implements JPAService<T, ID>, ApplicationContextAware {
    public EntityManager entityManager;
    public SimpleJpaRepository<T, ID> simpleJpaRepository;
    public ApplicationContext applicationContext;
    public Class currentEntityClass;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        synchronized (JPAServiceImpl.class) {
            if (simpleJpaRepositoryMap.size()==0) {
                Collection<EntityManager> entityManagers = applicationContext.getBeansOfType(EntityManager.class).values();
                for (EntityManager entityManager : entityManagers) {
                    // 参考了 https://blog.csdn.net/g_lfei/article/details/81776083 的逻辑, 根据Java类获取对应数据库的表名
                    // entityClass的class与tableName的映射
                    MetamodelImpl metamodelImpl = (MetamodelImpl)entityManager.getMetamodel();
                    Map<String, EntityPersister> persisterMap = metamodelImpl.entityPersisters();
                    for(Map.Entry<String, EntityPersister> entity : persisterMap.entrySet()) {
                        Class targetClass = entity.getValue().getMappedClass();
                        SingleTableEntityPersister persister = (SingleTableEntityPersister) entity.getValue();
//                        String entityName = targetClass.getSimpleName();//Entity的名称
//                        String tableName = persister.getTableName();//Entity对应的表的英文名
//                        System.out.println("类名：" + targetClass + " => 表名：" + persister.getTableName());
                        entityClassTableNameMap.put(targetClass, persister.getTableName());
                    }

                    // entityClass的class与entityManager的映射
                    Set<EntityType<?>> set = entityManager.getMetamodel().getEntities();
                    for (EntityType entityType : set) {
                        entityManagerMap.put(entityType.getJavaType(), entityManager);
                        simpleJpaRepositoryMap.put(entityType.getJavaType(), new SimpleJpaRepository(entityType.getJavaType(), entityManager));
                    }
                }
            }
        }
        entityManager = entityManagerMap.get(getGenericEntityClass());
        simpleJpaRepository = simpleJpaRepositoryMap.get(getGenericEntityClass());
    }

    // 获取泛型的具体实现类,Spring的提供工具类,用于获取继承的父类是泛型的信息
    public Class getGenericEntityClass() {
        if (currentEntityClass==null) {
            ResolvableType resolvableType = ResolvableType.forClass(getClass());
            currentEntityClass = resolvableType.getSuperType().getGeneric(0).resolve();
        }
        return currentEntityClass;
    }

    @Override
    public T find(ID id) {
        Assert.notNull(id, "id must not be null!");
        Specification specification = Specifications.and().eq("id", id).eq("del", false).build();
        Optional<T> optional = simpleJpaRepository.findOne(specification);
//        Optional<T> optional = simpleJpaRepository.findById(id);
        return optional.isPresent()? optional.get():null;
    }

    @Override
    @Transactional
    public T save(T entity) {
        Assert.notNull(entity, "entity must not be null!");
        entity.setDel(false);
        return simpleJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public List<T> save(T[] entities) {
        Assert.notNull(entities, "entities must not be null!");
        for (T item:entities) {
            item.setDel(false);
        }
        return simpleJpaRepository.saveAll(Arrays.asList(entities));
    }

    @Override
    @Transactional
    public List<T> save(List<T> entities) {
        Assert.notNull(entities, "entities must not be null!");
        for (T item:entities) {
            item.setDel(false);
        }
        return simpleJpaRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        Assert.notNull(entity, "entity must not be null!");
        simpleJpaRepository.delete(entity);
    }

    @Override
    @Transactional
    public void delete(T[] entities) {
        Assert.notNull(entities, "entities must not be null!");
        delete(Arrays.asList(entities));
    }

    @Override
    @Transactional
    public void delete(List<T> entities) {
        for (T item:entities) {
            item.setDel(true);
        }
        simpleJpaRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        T entity = Optional.of(find(id)).orElseThrow(new ResourcesNotFoundException());
        entity.setDel(true);
        simpleJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(ID[] ids) {
        Assert.notNull(ids, "Ids must not be null!");
        deleteById(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public void deleteById(List<ID> ids) {
        Assert.notNull(ids, "Ids must not be null!");
        delete(simpleJpaRepository.findAllById(ids));
    }

    @Override
    public Page<T> page(Pageable pageable) {
        return simpleJpaRepository.findAll(pageable);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public EntityManager getEntityManager(Class clazz) {
        return entityManagerMap.get(clazz);
    }

    @Override
    public SimpleJpaRepository<T, ID> getRepository() {
        return simpleJpaRepository;
    }

    @Override
    public SimpleJpaRepository<T, ID> getRepository(Class clazz) {
        return simpleJpaRepositoryMap.get(clazz);
    }
}
