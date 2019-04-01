package com.feiyu.common.base;

import com.baomidou.mybatisplus.plugins.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author feiyu127@gmail.com
 * @date 2018-08-28 11:29
 */
public interface BaseService<T> {
    /**
     * 添加单个实体
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 根据id更新实体
     * @param entity
     * @return
     */
    int updateById(T entity);

    /**
     * 根据id删除实体
     * @param id
     * @return
     */
    int deleteById(Serializable id);

    /**
     * 根据id获取单个实体bean
     * @param id
     * @return
     */
    T selectById(Serializable id);

    /**
     * 根据map搜索实体列表
     * @param map
     * @return
     */
    List<T> selectByMap(Map<String, Object> map);
    /**
     * 根据 entity 搜索实体列表
     * @param entity
     * @return
     */
    List<T> selectByEntity(T entity);

    Page<T> selectPageByEntity(T entity);
}
